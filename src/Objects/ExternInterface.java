package Objects;

import javax.sound.midi.*;
import javax.sound.sampled.*;
import java.util.ArrayList;

public class ExternInterface {

    private MidiDevice MidiInput = null;
    private MidiDevice MidiOutput = null;

    // Volume interface
    private Mixer mixer = null;
    private Mixer microphone = null;

    public FloatControl volume = null;
    private Line line = null;

    private ArrayList<Mixer> outputMixers;
    private ArrayList<Mixer> inputMixers;
    private ArrayList<MidiDevice> inputMidiDevice;
    private ArrayList<MidiDevice> outputMidiDevice;


    public ExternInterface() {
        this.outputMixers = new ArrayList<>();
        this.inputMixers = new ArrayList<>();
        this.inputMidiDevice = new ArrayList<>();
        this.outputMidiDevice = new ArrayList<>();

        initMidiDevice();
        initMixers();

        initKeyboard();
    }

    private void initMidiDevice()
    {
        // On récupère les midi devices
        MidiDevice midiDevice;
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();

        for (int i = 0; i < infos.length; i++) {
            try {
                midiDevice = MidiSystem.getMidiDevice(infos[i]);

                if ( midiDevice.getMaxTransmitters() != 0 && ! midiDevice.isOpen())
                {
                    this.inputMidiDevice.add(midiDevice);
                    //if ( this.MidiInput == null ) setMidiDevice(midiDevice);
                }
                if ( midiDevice.getMaxReceivers() != 0 && ! midiDevice.isOpen())
                {
                    this.outputMidiDevice.add(midiDevice);
                    //if ( this.MidiOutput == null ) setMidiDevice(midiDevice);
                }

                // If no device set

            } catch (MidiUnavailableException ignored) {}
        }
    }

    private void initMixers()
    {
        Mixer.Info[] mixers = AudioSystem.getMixerInfo();
        for (Mixer.Info mixerInfo : mixers) {
            // Getting the speaker
            Mixer mixer = AudioSystem.getMixer(mixerInfo);

            /*
            System.out.println("SPEAKER : " + mixer.isLineSupported(Port.Info.SPEAKER));
            System.out.println("MICROPHONE : " + mixer.isLineSupported(Port.Info.MICROPHONE));
            System.out.println("HEADPHONE : " + mixer.isLineSupported(Port.Info.HEADPHONE));
            System.out.println("LINE_OUT : " + mixer.isLineSupported(Port.Info.LINE_OUT));
            System.out.println("LINE_IN : " + mixer.isLineSupported(Port.Info.LINE_IN));
            System.out.println("COMPACT_DISC : " + mixer.isLineSupported(Port.Info.COMPACT_DISC));
            */

            // HEADPHONE SPEAKER
            if (mixer.isLineSupported(Port.Info.SPEAKER)) {
                this.outputMixers.add(mixer);

                // If no mixer output set
                if (this.mixer == null) this.setMixer(mixer);
            }
            // MICROPHONE MIXER TODO
            else if (mixer.isLineSupported(Port.Info.MICROPHONE)) {
                this.inputMixers.add(mixer);

                if (this.microphone == null) this.setMicrophone(mixer);
            }
        }
    }

    public void setMixer(Mixer mixer) {
        if ( this.line != null && this.line.isOpen())
            this.line.close();

        this.mixer = mixer;
        // Getting line infos list
        Line.Info [] lineInfos = mixer.getTargetLineInfo(); // target, not source

        for (Line.Info lineInfo : lineInfos) {
            boolean isOpen = true;
            Line line = null;

            try {
                line = mixer.getLine(lineInfo);
                isOpen = line.isOpen() || line instanceof Clip;

                if ( ! isOpen )
                    line.open();

                this.line = line;

                if ( ! System.getProperty("os.name").equals("Linux") )
                {
                    FloatControl volCtrl = (FloatControl) line.getControl(FloatControl.Type.VOLUME);
                    this.volume = volCtrl;
                }

            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        }
    }


    public void setMicrophone(Mixer mixer)
    {
        this.microphone = mixer;
    }

    public void setMidiDevice(MidiDevice device)
    {
        if ( this.MidiInput != null ) MidiInput.close();

        this.MidiInput = device;
        try {
            this.MidiInput.open();

            this.synthesizer = MidiSystem.getSynthesizer();
            this.synthesizer.open();
            this.midiChannel = this.synthesizer.getChannels()[0];

            this.receiver = new CustomReceiver(this.midiChannel);

            this.sequencer = MidiSystem.getSequencer();
            this.sequencer.open();;

            MidiSystem.getTransmitter().setReceiver(receiver);

        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void setMidiOutput(MidiDevice device)
    {
        if ( this.MidiOutput != null ) MidiOutput.close();

        this.MidiOutput = device;
        try {
            this.MidiOutput.open();
            System.out.println(this.MidiOutput.getDeviceInfo());
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
    }

    /*
     * RECORD METHOD
     */

    private Sequence sequence;
    public Sequencer sequencer;
    private Synthesizer synthesizer;
    private MidiChannel midiChannel;
    private CustomReceiver receiver;
    private Transmitter transmitter;


    public void initKeyboard()
    {
        try {
            this.synthesizer = MidiSystem.getSynthesizer();
            this.synthesizer.open();

            this.sequencer = MidiSystem.getSequencer();

            this.sequencer.open();

            MidiSystem.getTransmitter().setReceiver(this.receiver);

            this.midiChannel = this.synthesizer.getChannels()[0];

            this.receiver = new CustomReceiver(this.midiChannel);
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
    }



    public void startRecording()
    {
        if ( this.MidiInput == null || this.MidiOutput == null ) return;
        try {
            // Open a connection to your input device
            this.MidiInput.open();
            // Open a connection to the default sequencer (as specified by MidiSystem)
            sequencer.open();
            // Get the transmitter class from your input device
            transmitter = this.MidiInput.getTransmitter();
            // Get the receiver class from your sequencer
            Receiver receiver = sequencer.getReceiver();
            // Bind the transmitter to the receiver so the receiver gets input from the transmitter

            sequencer.addMetaEventListener(meta -> System.out.println("Metaa"));

            transmitter.setReceiver(receiver);

            // Create a new sequence
            this.sequence = new Sequence(Sequence.PPQ, 24);
            // And of course a track to record the input on
            Track currentTrack = sequence.createTrack();
            // Do some sequencer settings
            sequencer.setSequence(sequence);
            sequencer.setTickPosition(0);
            sequencer.recordEnable(currentTrack, -1);
            // And start recording
            sequencer.startRecording();
        } catch (InvalidMidiDataException | MidiUnavailableException e) {
            //e.printStackTrace();
        }
    }

    public Sequence stopRecording()
    {
        sequencer.stopRecording();

        System.out.println("Stoop");

        this.sequencer.setTickPosition(0);

        try {
            this.sequencer.setSequence(sequence);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
        this.sequencer.setTempoInBPM(120);
        this.sequencer.setLoopCount(1);

        this.sequencer.start();

        return sequencer.getSequence();
    }


    public ArrayList<MidiDevice> getInputMidiDevice() {
        return inputMidiDevice;
    }

    public ArrayList<MidiDevice> getOutputMidiDevice() {
        return outputMidiDevice;
    }

    public ArrayList<Mixer> getInputMixers() {
        return inputMixers;
    }

    public ArrayList<Mixer> getOutputMixers() {
        return outputMixers;
    }
}

package Objects;

import javax.sound.midi.*;
import javax.sound.sampled.*;
import java.util.ArrayList;

public class ExternInterface {

    public MidiDevice MidiInput = null;
    public MidiDevice MidiOutput = null;

    // Volume interface
    private Mixer mixer = null;
    private Mixer microphone = null;

    public FloatControl volume = null;
    private Line line = null;

    private ArrayList<Mixer> outputMixers;
    private ArrayList<Mixer> inputMixers;
    private ArrayList<MidiDevice> inputMidiDevice;
    private ArrayList<MidiDevice> outputMidiDevice;

    public Sequencer sequencer;
    private Synthesizer synthesizer;

    public ExternInterface() {
        this.outputMixers = new ArrayList<>();
        this.inputMixers = new ArrayList<>();
        this.inputMidiDevice = new ArrayList<>();
        this.outputMidiDevice = new ArrayList<>();

        initMidiDevice();
        initMixers();

        try {
            this.sequencer = MidiSystem.getSequencer();
            this.synthesizer = MidiSystem.getSynthesizer();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
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

    public void initKeyboard()
    {
        if ( this.MidiInput == null || this.MidiOutput == null ) return;

        // Get the transmitter class from your input device
        Transmitter transmitter = null;
        try {
            // Open a connection to your input device
            if ( ! this.MidiInput.isOpen() )
                this.MidiInput.open();

            if ( ! this.MidiOutput.isOpen() )
                this.MidiOutput.open();

            if ( ! this.synthesizer.isOpen() )
                this.synthesizer.open();

            // Open a connection to the default sequencer (as specified by MidiSystem)
            if ( ! this.sequencer.isOpen() )
                sequencer.open();

            transmitter = this.MidiInput.getTransmitter();
            // Get the receiver class from your sequencer
            Receiver receiver = sequencer.getReceiver();

            MidiChannel channel = synthesizer.getChannels()[0];
            CustomReceiver customReceiver = new CustomReceiver(channel, receiver);

            // Bind the transmitter to the receiver so the receiver gets input from the transmitter
            transmitter.setReceiver(customReceiver);
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
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

        initKeyboard();
    }

    public void setMidiOutput(MidiDevice device)
    {
        if ( this.MidiOutput != null ) MidiOutput.close();

        this.MidiOutput = device;
    }

    /*
     * RECORD METHOD
     */

    public void startRecording()
    {
        if ( this.MidiInput == null || this.MidiOutput == null ) return;

        try {
            // Create a new sequence
            Sequence sequence = new Sequence(Sequence.PPQ, 24);
            // And of course a track to record the input on
            Track currentTrack = sequence.createTrack();

            // Do some sequencer settings
            sequencer.setSequence(sequence);
            sequencer.setTickPosition(0);
            sequencer.recordEnable(currentTrack, -1);
            // And start recording
            sequencer.startRecording();
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }

    public Sequence stopRecording()
    {
        sequencer.stopRecording();

        this.sequencer.setTickPosition(0);

        Sequence sequence = this.sequencer.getSequence();

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

    public void close()
    {
        if ( this.MidiInput != null && this.MidiInput.isOpen() )
            this.MidiInput.close();

        if ( this.MidiOutput != null && this.MidiOutput.isOpen() )
            this.MidiOutput.close();

        if ( this.synthesizer != null && this.synthesizer.isOpen() )
            this.synthesizer.close();

        if ( this.sequencer != null && this.sequencer.isOpen() )
            this.sequencer.close();

        if ( this.line != null && this.line.isOpen() )
            this.line.close();

        if ( this.mixer != null && this.mixer.isOpen() )
            this.mixer.close();

        if ( this.microphone != null && this.microphone.isOpen() )
            this.microphone.close();
    }
}

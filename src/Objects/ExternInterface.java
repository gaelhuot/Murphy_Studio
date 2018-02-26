package Objects;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.sampled.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ExternInterface {

    private MidiDevice MidiInput = null;

    // Volume interface
    private Mixer mixer = null;
    private Mixer microphone = null;

    public FloatControl volume = null;
    private Line line = null;

    private ArrayList<Mixer> outputMixers;
    private ArrayList<Mixer> inputMixers;
    private ArrayList<MidiDevice> inputMidiDevice;


    public ExternInterface() {
        this.outputMixers = new ArrayList<>();
        this.inputMixers = new ArrayList<>();
        this.inputMidiDevice = new ArrayList<>();

        initMidiDevice();
        initMixers();
    }

    private void initMidiDevice()
    {
        // On récupère les midi devices
        MidiDevice midiDevice;
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();

        for (int i = 0; i < infos.length; i++) {
            try {
                midiDevice = MidiSystem.getMidiDevice(infos[i]);

                this.inputMidiDevice.add(midiDevice);

                // If no device set
                if ( this.MidiInput == null ) setMidiDevice(midiDevice);

            } catch (MidiUnavailableException ignored) {}
        }
    }

    private void initMixers()
    {
        Mixer.Info[] mixers = AudioSystem.getMixerInfo();
        for (Mixer.Info mixerInfo : mixers) {
            // Getting the speaker
            Mixer mixer = AudioSystem.getMixer(mixerInfo);

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
                FloatControl volCtrl = (FloatControl) line.getControl(FloatControl.Type.VOLUME);
                this.volume = volCtrl;

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
        this.MidiInput = device;
    }

    public void setMidiDevice(int index)
    {
        this.MidiInput = inputMidiDevice.get(index);
    }

    public ArrayList<MidiDevice> getInputMidiDevice() {
        return inputMidiDevice;
    }

    public ArrayList<Mixer> getInputMixers() {
        return inputMixers;
    }

    public ArrayList<Mixer> getOutputMixers() {
        return outputMixers;
    }
}

package net.hokuspokus.wott.client;

import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import jmetest.audio.TestJmexAudio;

import com.jme.renderer.Camera;
import com.jme.scene.Spatial;
import com.jmex.audio.AudioSystem;
import com.jmex.audio.AudioTrack;
import com.jmex.audio.EnvironmentalPool;
import com.jmex.audio.RangedAudioTracker;
import com.jmex.audio.AudioTrack.TrackType;
import com.jmex.audio.MusicTrackQueue.RepeatType;
import com.jmex.audio.openal.OpenALPropertyTool;

/**
 * Class to take care of starting/stopping sounds.
 * @author emanuel
 *
 */
public class SoundCenter implements ChangeListener
{
	private AudioSystem audio;
    private ArrayList<RangedAudioTracker> trackers = new ArrayList<RangedAudioTracker>();
    private Hashtable<String, AudioTrack> current_env = new Hashtable<String, AudioTrack>();
	private Camera cam;
	private static SoundCenter instance;

	public SoundCenter(Camera cam)
	{
		// grab a handle to our audio system.
		audio = AudioSystem.getSystem();
		this.cam = cam;
		
		// HACK: improved the Audio
		System.out.println("audio:"+audio);
		
		// Setupd the ears
        audio.getEar().trackOrientation(cam);
        audio.getEar().trackPosition(cam);
        
        audio.getEnvironmentalPool().addSongListChangeListener(this);
        //audio.setMasterGain(0.8f);
        
        instance = this;
	}

	public void enqueueSound(String ... music)
	{
		// setup a music score for our demo
		audio.getMusicQueue().setRepeatType(RepeatType.ALL);
		audio.getMusicQueue().setCrossfadeinTime(2.5f);
		audio.getMusicQueue().setCrossfadeoutTime(2.5f);
		for(String m : music)
		{
			AudioTrack music1 = getMusic(getResource(m));
			audio.getMusicQueue().addTrack(music1);
		}
		audio.getMusicQueue().play();
	}

	public void playSound(String sound, Spatial emitter, boolean loop)
	{
		System.err.println("sound:"+sound);
		AudioTrack sfx2 = current_env.get(sound);
		if(sfx2 == null)
		{
			sfx2 = getSFX(getResource(sound), TrackType.ENVIRONMENT, loop);
			sfx2.setMaxAudibleDistance(1000);
			sfx2.setVolume(1.0f);
			//current_env.put(sound, sfx2);
			//audio.getEnvironmentalPool().addTrack(sfx2);
		}
		sfx2.setWorldPosition(cam.getLocation());
		sfx2.play();
	}
	
	private URL getResource(String sound)
	{
		return SoundCenter.class.getClassLoader().getResource(sound);
	}

	private AudioTrack getSFX(URL resource, TrackType type, boolean loop)
	{
		// Create a non-streaming, looping, positional sound clip.
		AudioTrack sound = AudioSystem.getSystem().createAudioTrack(resource, false);
		sound.setType(type);
		sound.setLooping(loop);
		return sound;
	}
	


	private AudioTrack getMusic(URL resource)
	{
		// Create a non-streaming, non-looping, relative sound clip.
		AudioTrack sound = AudioSystem.getSystem().createAudioTrack(resource, false);
		sound.setType(TrackType.MUSIC);
		sound.setRelative(true);
		sound.setTargetVolume(0.7f);
		sound.setLooping(false);
		return sound;
	}

	
	public void updateSounds(float dt, Camera cam)
	{
		audio.update();
        for (int x = trackers.size(); --x >= 0; ) {
            RangedAudioTracker t = trackers .get(x);
            t.checkTrackAudible(cam.getLocation());
        }
	}

	@Override
	protected void finalize() throws Throwable
	{
		super.finalize();
		audio.cleanup();
	}

	public void stateChanged(ChangeEvent e)
	{
		System.out.println("e:"+e);
		System.out.println("e:"+e.getSource());	
		if(e.getSource() instanceof EnvironmentalPool)
		{
			EnvironmentalPool ep = (EnvironmentalPool) e.getSource();
			//ep.clearTracks(); // HACK!
			//ep.
			//audio.getEnvironmentalPool().removeTrack(track)
		}
	}

	public static SoundCenter getInstance()
	{
		return instance;
	}
}

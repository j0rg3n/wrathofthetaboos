package net.hokuspokus.wott.client;

import java.net.URL;
import java.util.ArrayList;

import jmetest.audio.TestJmexAudio;

import com.jme.renderer.Camera;
import com.jme.scene.Spatial;
import com.jmex.audio.AudioSystem;
import com.jmex.audio.AudioTrack;
import com.jmex.audio.RangedAudioTracker;
import com.jmex.audio.AudioTrack.TrackType;
import com.jmex.audio.MusicTrackQueue.RepeatType;

/**
 * Class to take care of starting/stopping sounds.
 * @author emanuel
 *
 */
public class SoundCenter
{
	private AudioSystem audio;
    private ArrayList<RangedAudioTracker> trackers = new ArrayList<RangedAudioTracker>();

	public SoundCenter(Camera cam)
	{
		// grab a handle to our audio system.
		audio = AudioSystem.getSystem();
		
		// Setupd the ears
        audio.getEar().trackOrientation(cam);
        audio.getEar().trackPosition(cam);
	}

	public void enqueueSound(String music)
	{
		// setup a music score for our demo
		AudioTrack music1 = getMusic(getResource(music));
		audio.getMusicQueue().setRepeatType(RepeatType.ALL);
		audio.getMusicQueue().setCrossfadeinTime(2.5f);
		audio.getMusicQueue().setCrossfadeoutTime(2.5f);
		audio.getMusicQueue().addTrack(music1);
		audio.getMusicQueue().play();
	}

	public void playSound(String sound, Spatial emitter, boolean loop)
	{
		AudioTrack sfx2 = getSFX(getResource(sound), emitter != null ? TrackType.POSITIONAL : TrackType.HEADSPACE, loop);
		RangedAudioTracker track2 = new RangedAudioTracker(sfx2, 25, 30);
		if(emitter != null)
		{
			track2.setToTrack(emitter);
			track2.setTrackIn3D(true);
			trackers.add(track2);
		}
		else
		{
			track2.setTrackIn3D(false);
		}
		track2.setMaxVolume(1.0f);
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

	
	public void updateSounds(Camera cam)
	{
        for (int x = trackers.size(); --x >= 0; ) {
            RangedAudioTracker t = trackers .get(x);
            //t.checkTrackAudible(cam.getLocation());
        }
	}

	@Override
	protected void finalize() throws Throwable
	{
		super.finalize();
		audio.cleanup();
	}
}

package com.jasonette.seed.Action;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import com.jasonette.seed.Core.JasonViewActivity;
import com.jasonette.seed.Helper.JasonHelper;

import org.json.JSONObject;

import timber.log.Timber;

public class JasonAudioAction {
    private static final int NUM_OF_CHANNELS = 1;
    private static final int AUDIO_SAMPLE_RATE = 48 * 1000;
    private MediaPlayer player;
    private MediaRecorder recorder;

    public void play(final JSONObject action, JSONObject data, final JSONObject event, final Context context) {
        try {
            if (action.has("options")) {
                JSONObject options = action.getJSONObject("options");
                if(options.has("url")){
                    if(player == null) {
                        player = new MediaPlayer();
                        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

                        String url = options.getString("url");
                        player.reset();
                        player.setDataSource(url);
                        player.prepare();
                    }

                    AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                    ((JasonViewActivity)context).setVolumeControlStream(AudioManager.STREAM_MUSIC);

                    if(player.isPlaying()){
                        player.pause();
                    } else {
                        player.start();
                    }
                }
            }
            JasonHelper.next("success", action, new JSONObject(), event, context);
        } catch (SecurityException e){
            JasonHelper.permission_exception("$audio.play", context);
        } catch (Exception e) {
            Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
        }
    }
    public void pause(final JSONObject action, JSONObject data, final JSONObject event, final Context context) {
        if(player!=null){
            player.pause();
        }
        JasonHelper.next("success", action, new JSONObject(), event, context);
    }
    public void stop(final JSONObject action, JSONObject data, final JSONObject event, final Context context) {
        if(player!=null){
            player.stop();
            player.release();
            player = null;
        }
        JasonHelper.next("success", action, new JSONObject(), event, context);
    }
    public void duration(final JSONObject action, JSONObject data, final JSONObject event, final Context context) {
        if (player != null) {
            try {
                int duration = (int) (player.getDuration() / 1000);
                JSONObject ret = new JSONObject();
                ret.put("value", String.valueOf(duration));
                JasonHelper.next("success", action, ret, event, context);
            } catch (Exception e) {
                Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
                try {
                    JSONObject err = new JSONObject();
                    err.put("message", "invalid position");
                    JasonHelper.next("error", action, err, event, context);
                } catch (Exception e2){
                    Log.d("Warning", e2.getStackTrace()[0].getMethodName() + " : " + e2.toString());
                }
            }
        } else {
            try {
                JSONObject err = new JSONObject();
                err.put("message", "player doesn't exist");
                JasonHelper.next("error", action, err, event, context);
            } catch (Exception e){
                Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
            }
        }
    }
    public void position(final JSONObject action, JSONObject data, final JSONObject event, final Context context) {
        if (player != null) {
            try {
                int duration = player.getDuration();
                int position = player.getCurrentPosition();
                float ratio = position/duration;
                JSONObject ret = new JSONObject();
                ret.put("value", String.valueOf(ratio));
                JasonHelper.next("success", action, ret, event, context);
            } catch (Exception e) {
                try {
                    JSONObject err = new JSONObject();
                    err.put("message", "invalid position or duration");
                    JasonHelper.next("error", action, err, event, context);
                } catch (Exception e2){
                    Log.d("Warning", e2.getStackTrace()[0].getMethodName() + " : " + e2.toString());
                }
            }
        } else {
            try {
                JSONObject err = new JSONObject();
                err.put("message", "player doesn't exist");
                JasonHelper.next("error", action, err, event, context);
            } catch (Exception e){
                Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
            }
        }
    }
    public void seek(final JSONObject action, JSONObject data, final JSONObject event, final Context context) {
        if(player!=null) {
            try {
                if (action.has("options")) {
                    JSONObject options = action.getJSONObject("options");
                    if(options.has("position")){
                        float position = Float.parseFloat(options.getString("position"));
                        int duration = player.getDuration();
                        player.seekTo((int)position * duration);
                    }
                }
            } catch (Exception e) {
                Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
            }
        }
        JasonHelper.next("success", action, new JSONObject(), event, context);
    }

    public void record(final JSONObject action, JSONObject data, final JSONObject event, final Context context) {
        try {
//            int color = JasonHelper.parse_color("rgba(0,0,0,0.8)");
//            if (action.has("options")) {
//                JSONObject options = action.getJSONObject("options");
//                if (options.has("color")) {
//                    color = JasonHelper.parse_color(options.getString("color"));
//                }
//            }

            String filePath = Environment.getExternalStorageDirectory() + "/recorded_audio.m4a";
            int requestCode = (int)(System.currentTimeMillis() % 10000);

            if(recorder == null) {
                Timber.d("STARTING RECORDING");
                recorder = new MediaRecorder();
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                recorder.setAudioChannels(NUM_OF_CHANNELS);
                recorder.setAudioSamplingRate(AUDIO_SAMPLE_RATE);
                recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                recorder.setOutputFile(filePath);
                recorder.prepare();
                recorder.start();
            } else {
                Timber.d("STOPPING RECORDING");
                recorder.stop();
                recorder.release();
                recorder = null;

                JSONObject callback = new JSONObject();
                callback.put("class", "JasonAudioAction");
                callback.put("method", "process");
                JasonHelper.dispatchIntent(String.valueOf(requestCode), action, data, event, context, null, callback);
            }
        } catch (SecurityException e){
            JasonHelper.permission_exception("$audio.record", context);
        } catch (Exception e){
            Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
        }
    }
}

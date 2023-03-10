package com.intellijava.core;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.intellijava.core.controller.RemoteSpeechModel;
import com.intellijava.core.model.AudioResponse;
import com.intellijava.core.model.SpeechModels;
import com.intellijava.core.model.input.Text2SpeechInput;
import com.intellijava.core.model.input.Text2SpeechInput.Gender;
import com.intellijava.core.utils.AudioHelper;
import com.intellijava.core.utils.Config2;
import com.intellijava.core.wrappers.GoogleAIWrapper;

public class GoogleSpeechTest {
	
	private final String apiKey = Config2.getInstance().getProperty("url.google.testkey");
	
	@Test
	public void testAudioConversion() { 
		String audioContent = "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";
		byte[] decodedAudio = AudioHelper.decode(audioContent);
		assert AudioHelper.saveTempAudio(decodedAudio) == true;
		AudioHelper.deleteTempAudio();
	}
	
	@Test
	public void testText2MaleSpeechWrapper() {  
		
		GoogleAIWrapper wrapper = new GoogleAIWrapper(apiKey);
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("text", "Hi, I am Intelligent Java.");
			params.put("languageCode", "en-gb");
			params.put("name", "en-GB-Standard-B");
			params.put("ssmlGender", "MALE");
			
			AudioResponse resModel = (AudioResponse) wrapper.generateSpeech(params);
			assert resModel.getAudioContent().length() > 0;
			
			byte[] decodedAudio = AudioHelper.decode(resModel.getAudioContent());
			assert AudioHelper.saveTempAudio(decodedAudio) == true;
			AudioHelper.deleteTempAudio();
		} catch (IOException e) {
			if (apiKey.isBlank()) {
				System.out.print("testAudioWrapper set the API key to run the test case.");
			} else {
				fail("testAudioWrapper failed with exception: " + e.getMessage());
			}
		}
	}
	
	@Test
	public void testText2FemaleSpeechWrapper() {  
		
		GoogleAIWrapper wrapper = new GoogleAIWrapper(apiKey);
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("text", "Hi, I am Intelligent Java.");
			params.put("languageCode", "en-gb");
			params.put("name", "en-GB-Standard-A");
			params.put("ssmlGender", "FEMALE");
			
			AudioResponse resModel = (AudioResponse) wrapper.generateSpeech(params);
			assert resModel.getAudioContent().length() > 0;
			
			byte[] decodedAudio = AudioHelper.decode(resModel.getAudioContent());
			assert AudioHelper.saveTempAudio(decodedAudio) == true;
			AudioHelper.deleteTempAudio();
		} catch (IOException e) {
			if (apiKey.isBlank()) {
				System.out.print("testAudioWrapper set the API key to run the test case.");
			} else {
				fail("testAudioWrapper failed with exception: " + e.getMessage());
			}
		}
	}
	
	@Test
	public void testText2FemaleRemoteSpeecModel() {  
		Text2SpeechInput input = new Text2SpeechInput.Builder("Hi, I am Intelligent Java.").
				setGender(Gender.FEMALE).build();
		
		RemoteSpeechModel model = new RemoteSpeechModel(apiKey, SpeechModels.google);
		
		try {
			byte[] decodedAudio = model.generateEnglishText(input);
			assert AudioHelper.saveTempAudio(decodedAudio) == true;
			AudioHelper.deleteTempAudio();
		} catch (IOException e) {
			if (apiKey.isBlank()) {
				System.out.print("testRemoteSpeech set the API key to run the test case.");
			} else {
				fail("testRemoteSpeech failed with exception: " + e.getMessage());
			}
		}
	}
	
	@Test
	public void testText2FemaleRemoteSpeecModel2() {  
		Text2SpeechInput input = new Text2SpeechInput("Hi, I am Intelligent Java.", Gender.MALE);
		
		RemoteSpeechModel model = new RemoteSpeechModel(apiKey, SpeechModels.google);
		
		try {
			byte[] decodedAudio = model.generateEnglishText(input);
			assert AudioHelper.saveTempAudio(decodedAudio) == true;
			AudioHelper.deleteTempAudio();
		} catch (IOException e) {
			if (apiKey.isBlank()) {
				System.out.print("testRemoteSpeech set the API key to run the test case.");
			} else {
				fail("testRemoteSpeech failed with exception: " + e.getMessage());
			}
		}
	}
}

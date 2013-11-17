package eu.squ1rr.uni.chatbox;

import java.util.ArrayList;
import java.util.Date;

/**
 * Messages generator, NOT optimised, why? it's temporal
 * 
 * @author Aleksandr Belkin
 * copyright (c) 2013, Bournemouth
 */
public class FakeServer {
	
	/**
	 * Possible sender names
	 */
	private static final String[] senderNames = {
		"Alex", "Bob", "Charlie",
		"Frank", "Harry", "Paul", "Crazy",
		"David", "Tom", "Charlz", "James",
		"Stacey", "Stanley", "Felix", "Toyn",
		"John", "Daniel", "Nikolai",
		"Dave", "Dangerous", "Skinny",
		"James", "Optimistic", "Sad", "Angry", "Lovely",
		"Shy", "Crying", "Pretty",
		"Shrimp", "Cry", "Warcraft",
	};
	
	private static final String[] senderSurnames = {
		"Belkin", "Milsom", "Levitate", "Crazy",
		"de Vrieze", "Lady Killer", "Gladwin",
		"Powers", "Tsarenko", "Travolta", "Einstein",
		"Faraday", "Heineken", "Random", "Marlboro",
		"Cameron", "Shrimp"
	};
	
	/**
	 * Possible message bodies
	 */
	private static final String[] bodies = {
		"Hello World", "Hit me", "Hide'n'seek",
		"Smile :-)", "Need coce", "Love it",
		"Wink ;-)", "Knock-knock", "Hey!",
		"Hey mate", "Who's there", "Me",
		"You alright?", "Stop touching me!",
		"What are you up to guys", "David!",
		"Is apple a fruit?", "Alex!", "Frank!",
		"Apple is a company :)", "Come here",
		"I love this app (:", "Who's that?",
		"This app is awesome (-:", "Is that you?",
		"Yet another smily test (-;", "Tell me more",
		"Thumb up -=b", "I'm not a robot, you are a robot",
		"-=b -> brofist, bro", "Googlebot is here",
		"d=- -> other fist mate", "Save your lifes",
		":):)(=--=b(:) ??", "And they lived",
		"tamato is a fruit", "Long and happy",
		"in Russia tomato is a berry", "Ever after",
		"in Soviet Russia apps debug you", "In Happiness",
		"Excellent mark for this assignment!", "And laughter",
		"Go ***k yourself, Alex", "Example", "Love this song",
		"Why are you so mean?", "Give me five!", "Daugh",
		"!!!", "???", "Give me smile", "Show me something",
		"Crazy people, voodoo people", "Nooooooo",
		"We are anonymous", "We are legion", "We do not forgive",
		"Religion is a lie", "We do not forget", "Java sucks",
		"Dance, baby, dance", "C++ is way much better", "Yea-yea",
		"Kill some shrimps", "Give me some", "You need some?",
		"I'm done playing", "I have some for you",
		"How did you debug this hell of a code, mate?",
		"Dunno, happens", "Red is bad, yellow is not",
		"spam spam spam spam", "Roses are red", "Violets are blue",
		"tro lo lo", "youtube.com rocks", "You've been hacked",
		"9gag rulz", "4chan is here", "Give me a break", "Yes!",
		"I'm oldskool b-e-a-c-h", "So what?", "I'm still a rockstart!",
		"I'm drinking from the bottle", "Hate this typing",
		"Привет, а я говорю по русский", "Too many letters",
		"I cannot understand you :(", "This chat is killing me",
		"Tell me something", "Hahahaha", ":):):):)", "Muhahahaha",
		"Do you smoke weed?", "lol", "rofl", "I lold", "maan, that's funny",
		"Yep!", "I just can't get enough", "This goes to the Internet",
		"Nooooo", "Snapchat", "Have you heard about it", "You moron",
		"I'm your father, MUHAHAHAA", "Don't talk to me",
		"Dexter Espid 4 season 6", "Ya mamma so fat",
		"Na-Na-Na-Na-Na-Na-Na-Na-Na-Na-Na-Na-Na Batman!"
	};
	
	/**
	 * Generates messages
	 * @param groupCount how many message groups to generate
	 * @param maxJoin maximum number of messages in each group
	 * @return generated message list
	 */
	public static ArrayList<Message> genMessages(int groupCount, int maxJoin) {
		ArrayList<Message> messages = new ArrayList<Message>();
		
		// current time-stamp in milliseconds
		long now = new Date().getTime();		
		
		// create {groupCount} groups
		for (int i = 0; i < groupCount; ++i) {
			// reduce time-stamp by a random amount of milliseconds
			long timeStamp = now - (int)(Math.random() * 120 * 60 * 1000);
			// identify random sender
			String name = senderNames[(int)(Math.random() * senderNames.length)];
			String sname = senderSurnames[(int)(Math.random() * senderSurnames.length)];
			String sender = name + " " + sname;
			
			// create 1..{maxJoin} messages for this group
			for (int j = 0; j < (int)(Math.random() * maxJoin + 1); ++j) {
				// identify random message text
				String body = bodies[(int)(Math.random() * bodies.length)];
				// increment time-stamp by a random amount of milliseconds
				timeStamp += (int)(Math.random() * 1000 * 30);
				
				// add message to the list
				messages.add(new Message(body, sender, timeStamp));
			}
		}
		
		return messages;
	}
}

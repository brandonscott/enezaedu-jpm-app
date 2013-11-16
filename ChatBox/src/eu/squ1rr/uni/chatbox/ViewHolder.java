package eu.squ1rr.uni.chatbox;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

/**
 * View holder of an Chat Box list view item
 * 
 * @author Aleksandr Belkin
 * copyright (c) 2013, Bournemouth
 */
public class ViewHolder {
	
	/*
	 * CONSTANTS
	 */
	
	/**
	 * Hard-coded colours
	 */
	private static final int[] COLOURS = {
		0xFF008299, 0xFF00A0B1, 0xFF2672EC, 0xFF2E8DEF,
		0xFF8C0095, 0xFFA700AE, 0xFF5133AB, 0xFF643EBF,
		0xFFAC193D, 0xFFBF1E4B, 0xFFD24726, 0xFFDC572E,
		0xFF008A00, 0xFF00A600, 0xFF094AB2, 0xFF0A5BC4,
		0xFFEEAAAA, 0xFFAAEEAA, 0xFFAAAAEE, 0xFF000000
	};
	
	/*
	 * INTERFACE MEMBERS
	 */
	
	/**
	 * User symbol for the message
	 */
	private TextView textSymbol;
	
	/**
	 * Message itself
	 */
	private TextView textMessage;
	
	/**
	 * Title of the message containing username and date
	 */
	private TextView textTitle;
	
	/**
	 * Time of the message
	 */
	private TextView textTime;
	
	/*
	 * MEMBERS
	 */
	
	/**
	 * For recycled views, we use two different layouts, so we need to know
	 * which one is this particular one
	 */
	private boolean even = true;
	
	/**
	 * Colour holder, holds assigned colours for every user, where user ID is
	 * {Symbol}{SenderName}
	 */
	private static HashMap<String, Integer> colours =
		new HashMap<String, Integer>();
	
	public ViewHolder(View view, boolean even) {
		this.even = even;
		
		textSymbol = (TextView)view.findViewById(R.id.textSymbol);
		textMessage = (TextView)view.findViewById(R.id.textMessage);
		
		textTitle = (TextView)view.findViewById(R.id.textTitle);
		textTime = (TextView)view.findViewById(R.id.textTime);
	}
	
	/**
	 * Set view to shown the given message
	 * @param chatMessage message to show
	 */
	public void setMessage(ChatMessage chatMessage) {
		// let's create symbol from the first character
		String sender = chatMessage.getSender();
		String symbol = sender.substring(0, 1);
		
		// if it was you who sent the message, change the output a bit
		if (sender.equals(Constants.ME)) {
			symbol = "·";
			sender = "You";
		}
		
		// format date in the way we want
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM yyyy", Locale.UK);
		SimpleDateFormat sdf2 = new SimpleDateFormat("kk:mm", Locale.UK);
		
		// convert time-stamp to a date
		Date date = new Date(chatMessage.getTimeStamp());
		
		// initialise interface with the values
		textSymbol.setText(symbol);
		textMessage.setText(chatMessage.getBody());
		textTitle.setText(sender + " (" + sdf1.format(date) + ")");
		textTime.setText(sdf2.format(date));
		
		// set symbol background
		setSymbol(sender, symbol);
	}
	
	/**
	 * @return if layout is on even position
	 */
	public boolean getEven() {
		return this.even;
	}
	
	/**
	 * Sets symbol background
	 * @param sender sender name
	 * @param symbol sender first character (for performance)
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private void setSymbol(String sender, String symbol) {
		Context context = textSymbol.getContext();
		ShapeDrawable symbolBackground = new ShapeDrawable();

		// get saved dimensions
		float dimension1 = context.getResources().getDimension(R.dimen.corner1);
		float dimension2 = context.getResources().getDimension(R.dimen.corner2);
		
		// swap them if the position is odd
		if (!even) {
			float temp = dimension1;
			dimension1 = dimension2;
			dimension2 = temp;
		}
		
		// create 8 radii
		float[] radii = new float[8];
		
		radii[0] = dimension1;
		radii[1] = dimension1;
		radii[2] = dimension2;
		radii[3] = dimension2;
		
		radii[4] = dimension1;
		radii[5] = dimension1;
		radii[6] = dimension2;
		radii[7] = dimension2;

		// initialise the shape
		symbolBackground.setShape(new RoundRectShape(radii, null, radii));
		symbolBackground.getPaint().setColor(generateColour(sender, symbol));

		// set the background (depending on a API version)
		if(Build.VERSION.SDK_INT >= 16) {
			textSymbol.setBackground(symbolBackground);
		} else {
			textSymbol.setBackgroundDrawable(symbolBackground);
		}
	}
	
	/**
	 * Returns colour for the given sender ID if it is already known, generates
	 * a new one otherwise
	 * @param sender sender name
	 * @param symbol sender first character (for performance)
	 * @return generated colour
	 */
	private int generateColour(String sender, String symbol) {
		// generate sender unique ID
		String id = symbol + sender;
		
		// return known colour if possible
		if (colours.get(id) != null) {
			return colours.get(id).intValue();
		} else {
			// generate and save colour
			int colour = COLOURS[(int)(Math.random() * COLOURS.length)];
			colours.put(id, colour);
			return colour;
		}
	}
}
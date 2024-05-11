package myGame;

import java.awt.Color;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.UUID;
import java.util.Vector;
import org.joml.*;

import java.lang.Math;

import tage.*;
import tage.networking.client.GameConnectionClient;

public class ProtocolClient extends GameConnectionClient
{
	private MyGame game;
	private GhostManager ghostManager;
	private UUID id;
	private GhostNPC ghostNPC, ghostNPC2, ghostNPC3;
	
	public ProtocolClient(InetAddress remoteAddr, int remotePort, ProtocolType protocolType, MyGame game) throws IOException 
	{	super(remoteAddr, remotePort, protocolType);
		this.game = game;
		this.id = UUID.randomUUID();
		ghostManager = game.getGhostManager();
	}
	
	public UUID getID() { return id; }

	// --- Ghost NPC Section ---
	private void createGhostNPC(Vector3f position) throws IOException {
		if (ghostNPC == null) {
			ghostNPC = new GhostNPC(0, game.getNPCshape(), game.getNPCtexture(), position);
		} 
		if (ghostNPC2 == null) {
			position.x = 9.0f;
			ghostNPC2 = new GhostNPC(1, game.getNPC2shape(), game.getNPC2texture(), position);
		}
		if (ghostNPC3 == null) {
			position.x = 9.0f;
			ghostNPC3 = new GhostNPC(2, game.getNPC2shape(), game.getNPC2texture(), position);
		}
	}

	private void updateGhostNPC(Vector3f position, double rotation) {
		boolean gs;

		if (ghostNPC == null) {
			try {
				createGhostNPC(position);
			} catch (IOException e) {
				System.out.println("error creating npc");
			}
		} 
		if (ghostNPC2 == null) {
			position.x = 9.0f;
			try {
				createGhostNPC(position);
			} catch (IOException e) {
				System.out.println("error creating npc");
			}
		}

		if (ghostNPC3 == null) {
			position.x = 9.0f;
			try {
				createGhostNPC(position);
			} catch (IOException e) {
				System.out.println("error creating npc");
			}
		}

		ghostNPC.setPosition(position);
		ghostNPC.setSize(0.2f);
		ghostNPC.setRotation(rotation);

		position.x = 9.0f;
		position.z += 2.5f;

		ghostNPC2.setPosition(position);
		ghostNPC2.setSize(0.2f);
		if (rotation >= 360) {
			rotation -= 180;
		} else {
			rotation += 180;
		}
		ghostNPC2.setRotation(rotation);

		position.x = 9.0f;
		position.z += 5.0f;

		ghostNPC3.setPosition(position);
		ghostNPC3.setSize(0.2f);
		if (rotation >= 360) {
			rotation -= 180;
		} else {
			rotation += 180;
		}
		ghostNPC3.setRotation(rotation);

		honk(ghostNPC);
		honk(ghostNPC2);
		honk(ghostNPC3);
	}

	// Honk if avatar is near
	public void honk(GhostNPC npc) {
		// Calculate the distance between the avatar and the ghost
		float distance = npc.getWorldLocation().distance(game.getAvatar().getWorldLocation());
		int score = game.getScore();

		if (distance < 2.0) {
			if (!game.getHonk().getIsPlaying()) {
				game.getHonk().play();
			}
		}

		if (distance < 0.15) {
			score -= 1.0;
			game.setScore(score);
		}
	}
	
	@Override
	protected void processPacket(Object message)
	{	String strMessage = (String)message;
		System.out.println("message received -->" + strMessage);
		String[] messageTokens = strMessage.split(",");
		
		// Game specific protocol to handle the message
		if(messageTokens.length > 0)
		{
			// Handle JOIN message
			// Format: (join,success) or (join,failure)
			if(messageTokens[0].compareTo("join") == 0)
			{	if(messageTokens[1].compareTo("success") == 0)
				{	System.out.println("join success confirmed");
					game.setIsConnected(true);
					sendCreateMessage(game.getPlayerPosition());
				}
				if(messageTokens[1].compareTo("failure") == 0)
				{	System.out.println("join failure confirmed");
					game.setIsConnected(false);
			}	}
			
			// Handle BYE message
			// Format: (bye,remoteId)
			if(messageTokens[0].compareTo("bye") == 0)
			{	// remove ghost avatar with id = remoteId
				// Parse out the id into a UUID
				UUID ghostID = UUID.fromString(messageTokens[1]);
				ghostManager.removeGhostAvatar(ghostID);
			}
			
			// Handle CREATE message
			// Format: (create,remoteId,x,y,z)
			// AND
			// Handle DETAILS_FOR message
			// Format: (dsfr,remoteId,x,y,z)
			if (messageTokens[0].compareTo("create") == 0 || (messageTokens[0].compareTo("dsfr") == 0))
			{	// create a new ghost avatar
				// Parse out the id into a UUID
				UUID ghostID = UUID.fromString(messageTokens[1]);
				
				// Parse out the position into a Vector3f
				Vector3f ghostPosition = new Vector3f(
					Float.parseFloat(messageTokens[2]),
					Float.parseFloat(messageTokens[3]),
					Float.parseFloat(messageTokens[4]));

				try
				{	ghostManager.createGhostAvatar(ghostID, ghostPosition);
				}	catch (IOException e)
				{	System.out.println("error creating ghost avatar");
				}
			}
			
			// Handle WANTS_DETAILS message
			// Format: (wsds,remoteId)
			if (messageTokens[0].compareTo("wsds") == 0)
			{
				// Send the local client's avatar's information
				// Parse out the id into a UUID
				UUID ghostID = UUID.fromString(messageTokens[1]);
				sendDetailsForMessage(ghostID, game.getPlayerPosition());
			}
			
			// Handle MOVE message
			// Format: (move,remoteId,x,y,z)
			if (messageTokens[0].compareTo("move") == 0)
			{
				// move a ghost avatar
				// Parse out the id into a UUID
				UUID ghostID = UUID.fromString(messageTokens[1]);
				
				// Parse out the position into a Vector3f
				Vector3f ghostPosition = new Vector3f(
					Float.parseFloat(messageTokens[2]),
					Float.parseFloat(messageTokens[3]),
					Float.parseFloat(messageTokens[4]));
				
				ghostManager.updateGhostAvatar(ghostID, ghostPosition);
			}

			// handle NPC ghosts
			if (messageTokens[0].compareTo("createNPC") == 0) {
				// create a new ghost NPC
				// Parse out the position into a Vector3f
				Vector3f ghostPosition = new Vector3f(
					Float.parseFloat(messageTokens[1]),
					Float.parseFloat(messageTokens[2]),
					Float.parseFloat(messageTokens[3]));
				try {
					createGhostNPC(ghostPosition);
				} catch (IOException e) {
					System.out.println("error creating npc");
				}
			}

			// trying to move npc
			if (messageTokens[0].compareTo("npcinfo") == 0) {
				// move a ghost NPC
				// Parse out the position into a Vector3f
				Vector3f ghostPosition = new Vector3f(
					Float.parseFloat(messageTokens[1]),
					Float.parseFloat(messageTokens[2]),
					Float.parseFloat(messageTokens[3]));
				double rotation = Double.parseDouble(messageTokens[4]);
				updateGhostNPC(ghostPosition, rotation);
			}

			// Handle MNPC message
			// Format: (mnpc,remoteId,x,y,z,gsize)
			if (messageTokens[0].compareTo("mnpc") == 0)
			{
				// move a ghost NPC
				// Parse out the id into a UUID
				UUID ghostID = UUID.fromString(messageTokens[1]);
			
				// Parse out the position into a Vector3f
				Vector3f ghostPosition = new Vector3f(
					Float.parseFloat(messageTokens[2]),
					Float.parseFloat(messageTokens[3]),
					Float.parseFloat(messageTokens[4]));
			
				// Parse out the size
				double gsize = Double.parseDouble(messageTokens[5]);
			
				updateGhostNPC(ghostPosition, gsize);
			}
			
			// Handle ISNEAR message
			// Format: (isnear,remoteId)
			if (messageTokens[0].compareTo("isnear") == 0) {
				// check if the player is near the ghost NPC
				Vector3f playerPosition = new Vector3f(
				Float.parseFloat(messageTokens[1]),
				Float.parseFloat(messageTokens[2]),
				Float.parseFloat(messageTokens[3]));
				double distance = Double.parseDouble(messageTokens[4]);
				if (game.getPlayerPosition().distance(playerPosition) < distance) {
					try {
						sendPacket(new String("isnear," + id.toString()));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
	} else {
		System.out.println("invalid message format: " + strMessage);
	}	}
	
	// The initial message from the game client requesting to join the 
	// server. localId is a unique identifier for the client. Recommend 
	// a random UUID.
	// Message Format: (join,localId)
	
	public void sendJoinMessage()
	{	try 
		{	sendPacket(new String("join," + id.toString()));
		} catch (IOException e) 
		{	e.printStackTrace();
	}	}
	
	// Informs the server that the client is leaving the server. 
	// Message Format: (bye,localId)

	public void sendByeMessage()
	{	try 
		{	sendPacket(new String("bye," + id.toString()));
		} catch (IOException e) 
		{	e.printStackTrace();
	}	}
	
	// Informs the server of the client’s Avatar’s position. The server 
	// takes this message and forwards it to all other clients registered 
	// with the server.
	// Message Format: (create,localId,x,y,z) where x, y, and z represent the position

	public void sendCreateMessage(Vector3f position)
	{	try 
		{	String message = new String("create," + id.toString());
			message += "," + position.x();
			message += "," + position.y();
			message += "," + position.z();
			
			sendPacket(message);
		} catch (IOException e) 
		{	e.printStackTrace();
	}	}
	
	// Informs the server of the local avatar's position. The server then 
	// forwards this message to the client with the ID value matching remoteId. 
	// This message is generated in response to receiving a WANTS_DETAILS message 
	// from the server.
	// Message Format: (dsfr,remoteId,localId,x,y,z) where x, y, and z represent the position.

	public void sendDetailsForMessage(UUID remoteId, Vector3f position)
	{	try 
		{	String message = new String("dsfr," + remoteId.toString() + "," + id.toString());
			message += "," + position.x();
			message += "," + position.y();
			message += "," + position.z();
			
			sendPacket(message);
		} catch (IOException e) 
		{	e.printStackTrace();
	}	}
	
	// Informs the server that the local avatar has changed position.  
	// Message Format: (move,localId,x,y,z) where x, y, and z represent the position.

	public void sendMoveMessage(Vector3f position)
	{	try 
		{	String message = new String("move," + id.toString());
			message += "," + position.x();
			message += "," + position.y();
			message += "," + position.z();
			
			sendPacket(message);
		} catch (IOException e) 
		{	e.printStackTrace();
	}	}
}

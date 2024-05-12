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
	private GhostNPC ghostNPC, ghostNPC2, ghostNPC3, ghostNPC4, ghostNPC5, ghostNPC6, ghostNPC7, ghostNPC8, ghostNPC9, ghostNPC10, ghostNPC11, ghostNPC12, ghostNPC13, ghostNPC14, ghostNPC15;
	
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
		if (ghostNPC4 == null) {
			position.x = 9.0f;
			ghostNPC4 = new GhostNPC(3, game.getNPCshape(), game.getNPCtexture(), position);
		}
		if (ghostNPC5 == null) {
			position.x = 9.0f;
			ghostNPC5 = new GhostNPC(4, game.getNPCshape(), game.getNPCtexture(), position);
		}
		if (ghostNPC6 == null) {
			position.x = 9.0f;
			ghostNPC6 = new GhostNPC(5, game.getNPC2shape(), game.getNPC2texture(), position);
		}
		if (ghostNPC7 == null) {
			position.x = 9.0f;
			ghostNPC7 = new GhostNPC(6, game.getNPC2shape(), game.getNPC2texture(), position);
		}
		if (ghostNPC8 == null) {
			position.x = 9.0f;
			ghostNPC8 = new GhostNPC(7, game.getNPCshape(), game.getNPCtexture(), position);
		}
		if (ghostNPC9 == null) {
			position.x = 9.0f;
			ghostNPC9 = new GhostNPC(8, game.getNPCshape(), game.getNPCtexture(), position);
		}
		if (ghostNPC10 == null) {
			position.x = 9.0f;
			ghostNPC10 = new GhostNPC(9, game.getNPC2shape(), game.getNPC2texture(), position);
		}
		if (ghostNPC11 == null) {
			position.x = 9.0f;
			ghostNPC11 = new GhostNPC(10, game.getNPC2shape(), game.getNPC2texture(), position);
		}
		if (ghostNPC12 == null) {
			position.x = 9.0f;
			ghostNPC12 = new GhostNPC(11, game.getNPCshape(), game.getNPCtexture(), position);
		}
		if (ghostNPC13 == null) {
			position.x = 9.0f;
			ghostNPC13 = new GhostNPC(12, game.getNPCshape(), game.getNPCtexture(), position);
		}
		if (ghostNPC14 == null) {
			position.x = 9.0f;
			ghostNPC14 = new GhostNPC(13, game.getNPC2shape(), game.getNPC2texture(), position);
		}
		if (ghostNPC15 == null) {
			position.x = 9.0f;
			ghostNPC15 = new GhostNPC(14, game.getNPC2shape(), game.getNPC2texture(), position);
		}
	}

	private void updateGhostNPC(Vector3f position, double rotation) {
		boolean gs;

		position.z -= 50.0f;

		if (ghostNPC == null) {
			float height = game.getTerrain().getHeight(position.x, position.z);
			position.y = height;
			try {
				createGhostNPC(position);
			} catch (IOException e) {
				System.out.println("error creating npc");
			}
		} 
		if (ghostNPC2 == null) {
			float height = game.getTerrain().getHeight(position.x, position.z);
			position.y = height;
			position.x = 9.0f;
			try {
				createGhostNPC(position);
			} catch (IOException e) {
				System.out.println("error creating npc");
			}
		}

		if (ghostNPC3 == null) {
			float height = game.getTerrain().getHeight(position.x, position.z);
			position.y = height;
			position.x = 11.5f;
			try {
				createGhostNPC(position);
			} catch (IOException e) {
				System.out.println("error creating npc");
			}
		}

		if (ghostNPC4 == null) {
			float height = game.getTerrain().getHeight(position.x, position.z);
			position.y = height;
			position.x = 9.0f;
			try {
				createGhostNPC(position);
			} catch (IOException e) {
				System.out.println("error creating npc");
			}
		}
		
		if (ghostNPC5 == null) {
			float height = game.getTerrain().getHeight(position.x, position.z);
			position.y = height;
			position.x = 11.5f;
			try {
				createGhostNPC(position);
			} catch (IOException e) {
				System.out.println("error creating npc");
			}
		}
		
		if (ghostNPC6 == null) {
			float height = game.getTerrain().getHeight(position.x, position.z);
			position.y = height;
			position.x = 9.0f;
			try {
				createGhostNPC(position);
			} catch (IOException e) {
				System.out.println("error creating npc");
			}
		}
		
		if (ghostNPC7 == null) {
			float height = game.getTerrain().getHeight(position.x, position.z);
			position.y = height;
			position.x = 11.5f;
			try {
				createGhostNPC(position);
			} catch (IOException e) {
				System.out.println("error creating npc");
			}
		}
		
		if (ghostNPC8 == null) {
			float height = game.getTerrain().getHeight(position.x, position.z);
			position.y = height;
			position.x = 9.0f;
			try {
				createGhostNPC(position);
			} catch (IOException e) {
				System.out.println("error creating npc");
			}
		}

		if (ghostNPC9 == null) {
			float height = game.getTerrain().getHeight(position.x, position.z);
			position.y = height;
			position.x = 11.5f;
			try {
				createGhostNPC(position);
			} catch (IOException e) {
				System.out.println("error creating npc");
			}
		}

		if (ghostNPC10 == null) {
			float height = game.getTerrain().getHeight(position.x, position.z);
			position.y = height;
			position.x = 9.0f;
			try {
				createGhostNPC(position);
			} catch (IOException e) {
				System.out.println("error creating npc");
			}
		}

		if (ghostNPC11 == null) {
			float height = game.getTerrain().getHeight(position.x, position.z);
			position.y = height;
			position.x = 11.5f;
			try {
				createGhostNPC(position);
			} catch (IOException e) {
				System.out.println("error creating npc");
			}
		}

		if (ghostNPC12 == null) {
			float height = game.getTerrain().getHeight(position.x, position.z);
			position.y = height;
			position.x = 9.0f;
			try {
				createGhostNPC(position);
			} catch (IOException e) {
				System.out.println("error creating npc");
			}
		}

		if (ghostNPC13 == null) {
			float height = game.getTerrain().getHeight(position.x, position.z);
			position.y = height;
			position.x = 11.5f;
			try {
				createGhostNPC(position);
			} catch (IOException e) {
				System.out.println("error creating npc");
			}
		}

		if (ghostNPC14 == null) {
			float height = game.getTerrain().getHeight(position.x, position.z);
			position.y = height;
			position.x = 9.0f;
			try {
				createGhostNPC(position);
			} catch (IOException e) {
				System.out.println("error creating npc");
			}
		}

		if (ghostNPC15 == null) {
			float height = game.getTerrain().getHeight(position.x, position.z);
			position.y = height;
			position.x = 11.5f;
			try {
				createGhostNPC(position);
			} catch (IOException e) {
				System.out.println("error creating npc");
			}
		}

		float height = game.getTerrain().getHeight(position.x, position.z);
		position.y = height;
		ghostNPC.setPosition(position);
		ghostNPC.setSize(0.2f);
		ghostNPC.setRotation(rotation);

		position.x = 9.0f;
		position.z += 2.5f;
		height = game.getTerrain().getHeight(position.x, position.z);
		position.y = height;

		ghostNPC2.setPosition(position);
		ghostNPC2.setSize(0.2f);
		if (rotation >= 360) {
			rotation -= 180;
		} else {
			rotation += 180;
		}
		ghostNPC2.setRotation(rotation);

		position.x = 11.5f;
		position.z += 7.5f;
		height = game.getTerrain().getHeight(position.x, position.z);
		position.y = height;

		ghostNPC3.setPosition(position);
		ghostNPC3.setSize(0.2f);
		if (rotation >= 360) {
			rotation -= 180;
		} else {
			rotation += 180;
		}
		ghostNPC3.setRotation(rotation);

		position.x = 9.0f;
		position.z += 7.5f;
		height = game.getTerrain().getHeight(position.x, position.z);
		position.y = height;

		ghostNPC4.setPosition(position);
		ghostNPC4.setSize(0.2f);
		if (rotation >= 360) {
			rotation -= 180;
		} else {
			rotation += 180;
		}
		ghostNPC4.setRotation(rotation);

		position.x = 11.5f;
		position.z += 7.5f;
		height = game.getTerrain().getHeight(position.x, position.z);
		position.y = height;

		ghostNPC5.setPosition(position);
		ghostNPC5.setSize(0.2f);
		if (rotation >= 360) {
			rotation -= 180;
		} else {
			rotation += 180;
		}
		ghostNPC5.setRotation(rotation);

		position.x = 9.0f;
		position.z += 7.5f;
		height = game.getTerrain().getHeight(position.x, position.z);
		position.y = height;

		ghostNPC6.setPosition(position);
		ghostNPC6.setSize(0.2f);
		if (rotation >= 360) {
			rotation -= 180;
		} else {
			rotation += 180;
		}
		ghostNPC6.setRotation(rotation);

		position.x = 11.5f;
		position.z += 7.5f;
		height = game.getTerrain().getHeight(position.x, position.z);
		position.y = height;

		ghostNPC7.setPosition(position);
		ghostNPC7.setSize(0.2f);
		if (rotation >= 360) {
			rotation -= 180;
		} else {
			rotation += 180;
		}
		ghostNPC7.setRotation(rotation);

		position.x = 9.0f;
		position.z += 7.5f;
		height = game.getTerrain().getHeight(position.x, position.z);
		position.y = height;

		ghostNPC8.setPosition(position);
		ghostNPC8.setSize(0.2f);
		if (rotation >= 360) {
			rotation -= 180;
		} else {
			rotation += 180;
		}
		ghostNPC8.setRotation(rotation);

		position.x = 11.5f;
		position.z += 7.5f;
		height = game.getTerrain().getHeight(position.x, position.z);
		position.y = height;

		ghostNPC9.setPosition(position);
		ghostNPC9.setSize(0.2f);
		if (rotation >= 360) {
			rotation -= 180;
		} else {
			rotation += 180;
		}
		ghostNPC9.setRotation(rotation);

		position.x = 9.0f;
		position.z += 7.5f;
		height = game.getTerrain().getHeight(position.x, position.z);
		position.y = height;

		ghostNPC10.setPosition(position);
		ghostNPC10.setSize(0.2f);
		if (rotation >= 360) {
			rotation -= 180;
		} else {
			rotation += 180;
		}
		ghostNPC10.setRotation(rotation);

		position.x = 11.5f;
		position.z += 7.5f;
		height = game.getTerrain().getHeight(position.x, position.z);
		position.y = height;

		ghostNPC11.setPosition(position);
		ghostNPC11.setSize(0.2f);
		if (rotation >= 360) {
			rotation -= 180;
		} else {
			rotation += 180;
		}
		ghostNPC11.setRotation(rotation);

		position.x = 9.0f;
		position.z += 7.5f;
		height = game.getTerrain().getHeight(position.x, position.z);
		position.y = height;

		ghostNPC12.setPosition(position);
		ghostNPC12.setSize(0.2f);
		if (rotation >= 360) {
			rotation -= 180;
		} else {
			rotation += 180;
		}
		ghostNPC12.setRotation(rotation);

		position.x = 11.5f;
		position.z += 7.5f;
		height = game.getTerrain().getHeight(position.x, position.z);
		position.y = height;

		ghostNPC13.setPosition(position);
		ghostNPC13.setSize(0.2f);
		if (rotation >= 360) {
			rotation -= 180;
		} else {
			rotation += 180;
		}
		ghostNPC13.setRotation(rotation);

		position.x = 9.0f;
		position.z += 7.5f;
		height = game.getTerrain().getHeight(position.x, position.z);
		position.y = height;

		ghostNPC14.setPosition(position);
		ghostNPC14.setSize(0.2f);
		if (rotation >= 360) {
			rotation -= 180;
		} else {
			rotation += 180;
		}
		ghostNPC14.setRotation(rotation);

		position.x = 11.5f;
		position.z += 7.5f;
		height = game.getTerrain().getHeight(position.x, position.z);
		position.y = height;

		ghostNPC15.setPosition(position);
		ghostNPC15.setSize(0.2f);
		if (rotation >= 360) {
			rotation -= 180;
		} else {
			rotation += 180;
		}
		ghostNPC15.setRotation(rotation);

		honk(ghostNPC);
		honk(ghostNPC2);
		honk(ghostNPC3);
		honk(ghostNPC4);
		honk(ghostNPC5);
		honk(ghostNPC6);
		honk(ghostNPC7);
		honk(ghostNPC8);
		honk(ghostNPC9);
		honk(ghostNPC10);
		honk(ghostNPC11);
		honk(ghostNPC12);
		honk(ghostNPC13);
		honk(ghostNPC14);
		honk(ghostNPC15);
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

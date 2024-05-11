package myGame;

import tage.*;
import tage.shapes.*;
import tage.input.*;
import tage.input.IInputManager.INPUT_ACTION_TYPE;
import tage.input.action.*;

import java.lang.Math;
import java.awt.*;

import java.awt.event.*;

import java.io.*;
import java.util.*;
import java.util.UUID;
import java.net.InetAddress;

import java.net.UnknownHostException;

import org.joml.*;

import net.java.games.input.*;
import net.java.games.input.Component.Identifier.*;
import net.java.games.input.Event;
import tage.networking.IGameConnection.ProtocolType;

// Physics
import tage.physics.PhysicsEngine;
import tage.physics.PhysicsObject;
import tage.physics.JBullet.*;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.collision.dispatch.CollisionObject;

// Audio
import tage.audio.*;

public class MyGame extends VariableFrameRateGame
{
	private static Engine engine;
	private InputManager im;
	private GhostManager gm;

	private int counter=0;
	private Vector3f currentPosition;
	private Matrix4f initialTranslation, initialRotation, initialScale;
	private double startTime, prevTime, elapsedTime, amt;

	private GameObject tor, avatar, x, y, z, terr, audience;
	private ObjShape torS, ghostS, dolS, linxS, linyS, linzS, terrS;
	private AnimatedShape audienceS;
	private TextureImage doltx, ghostT, hillsMap, hills, audienceT;
	private Light light, playerLight, ghostLight;

	private String serverAddress;
	private int serverPort;
	private ProtocolType serverProtocol;
	private ProtocolClient protClient;
	private boolean isClientConnected = false;
	private int skyLines;

	// For Physics
	private GameObject carNew;
	private ObjShape carShape;
	private TextureImage carTexture;

	private PhysicsEngine physicsEngine;
	private PhysicsObject avt, car, planeP, npcOne, npcTwo;
	private PhysicsObject cone1P, cone2P, cone3P, cone4P, cone5P, cone6P, cone7P, cone8P, cone9P, cone10P;

	private boolean running = false;
	private float vals[] = new float[16];

	// For Audio
	private IAudioManager audioMgr;
	private Sound engineSound, oceanSound, carSound, honkSound, bgSound;
	private Vector3f previousPosition = new Vector3f();

	// NPCs
	private GameObject npc1, npc2;
	private ObjShape npcShape, npcShape2;
	private TextureImage npcTex, npcTex2;

	// Camera
	private CameraOrbit3D orbitController;

	// Select Avatar
	private boolean selectAvatar = true;
	private String avatarName = "dol";

	private int score = 2;

	// Race line
	private GameObject raceLine, endLine;
	private ObjShape raceLineShape;
	private TextureImage raceLineTexture, endLineTexture;

	private boolean finish = false;
	private UUID ghostID;
	private boolean winS = false, winM = false;

	String time;

	// Obstacles
	private GameObject cone1, cone2, cone3, cone4, cone5, cone6, cone7, cone8, cone9, cone10;
	private ObjShape coneShape;
	private TextureImage coneTexture;

	public MyGame(String serverAddress, int serverPort, String protocol)
	{	super();
		gm = new GhostManager(this);
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
		if (protocol.toUpperCase().compareTo("TCP") == 0)
			this.serverProtocol = ProtocolType.TCP;
		else
			this.serverProtocol = ProtocolType.UDP;
	}

	public static void main(String[] args)
	{	MyGame game = new MyGame(args[0], Integer.parseInt(args[1]), args[2]);
		engine = new Engine(game);
		game.initializeSystem();
		game.game_loop();
	}

	@Override
	public void loadShapes()
	{	torS = new Torus(0.5f, 0.2f, 48);
		ghostS = new ImportedModel("greenCar.obj");
		dolS = new ImportedModel("car.obj");
		carShape = new ImportedModel("car.obj");
		linxS = new Line(new Vector3f(0f,0f,0f), new Vector3f(3f,0f,0f));
		linyS = new Line(new Vector3f(0f,0f,0f), new Vector3f(0f,3f,0f));
		linzS = new Line(new Vector3f(0f,0f,0f), new Vector3f(0f,0f,-3f));
		terrS = new TerrainPlane(1025);

		coneShape = new ImportedModel("cone.obj");

		raceLineShape = new ImportedModel("raceLine.obj");

		npcShape = new ImportedModel("greenCar.obj");
		npcShape2 = new ImportedModel("car.obj");

		audienceS = new AnimatedShape("npc.rkm", "npc.rks");
		audienceS.loadAnimation("clap", "clap.rka");
		audienceS.loadAnimation("jumping", "jumping.rka");
	}

	@Override
	public void loadTextures()
	{	doltx = new TextureImage("car.png");
		carTexture = new TextureImage("car.png");
		ghostT = new TextureImage("greenCar.jpg");
		hillsMap = new TextureImage("hillsmap.png");
		hills = new TextureImage("hills.jpg");

		raceLineTexture = new TextureImage("Start.png");
		endLineTexture = new TextureImage("Finish.png");

		coneTexture = new TextureImage("cone.png");

		npcTex = new TextureImage("greenCar.jpg");
		npcTex2 = new TextureImage("car.png");

		audienceT = new TextureImage("npc.jpg");
	}

	@Override
	public void loadSkyBoxes() {
		skyLines = (engine.getSceneGraph()).loadCubeMap("skyLines");
		(engine.getSceneGraph()).setActiveSkyBoxTexture(skyLines);	
		(engine.getSceneGraph()).setSkyBoxEnabled(true);
	}

	@Override
	public void buildObjects()
	{	Matrix4f initialTranslation, initialRotation, initialScale;

		// build avatar
		avatar = new GameObject(GameObject.root(), dolS, doltx);
		initialTranslation = (new Matrix4f()).translation(11.5f,0.5f,80.0f);
		avatar.setLocalTranslation(initialTranslation);
		initialRotation = (new Matrix4f()).rotationY((float)java.lang.Math.toRadians(180.0f));
		avatar.setLocalRotation(initialRotation);
		initialScale = (new Matrix4f()).scaling(0.20f);
		avatar.setLocalScale(initialScale);

		// build torus along X axis
		// tor = new GameObject(GameObject.root(), torS);
		// initialTranslation = (new Matrix4f()).translation(1,0,0);
		// tor.setLocalTranslation(initialTranslation);
		// initialScale = (new Matrix4f()).scaling(0.25f);
		// tor.setLocalScale(initialScale);

		// build car
		carNew = new GameObject(GameObject.root(), carShape, carTexture);
		initialTranslation = (new Matrix4f()).translation(9.0f,0.1f,-5.0f);
		carNew.setLocalTranslation(initialTranslation);
		initialRotation = (new Matrix4f()).rotationY((float)java.lang.Math.toRadians(180.0f));
		carNew.setLocalRotation(initialRotation);
		initialScale = (new Matrix4f()).scaling(0.20f);
		carNew.setLocalScale(initialScale);

		// build race line
		raceLine = new GameObject(GameObject.root(), raceLineShape, raceLineTexture);
		initialTranslation = (new Matrix4f()).translation(10.5f,0.5f,75.0f);
		raceLine.setLocalTranslation(initialTranslation);
		initialRotation = (new Matrix4f()).rotationY((float)java.lang.Math.toRadians(90.0f));
		raceLine.setLocalRotation(initialRotation);
		initialScale = (new Matrix4f()).scaling(0.2f);
		raceLine.setLocalScale(initialScale);

		endLine = new GameObject(GameObject.root(), raceLineShape, endLineTexture);
		initialTranslation = (new Matrix4f()).translation(10.5f,0.5f,-75.0f);
		endLine.setLocalTranslation(initialTranslation);
		initialRotation = (new Matrix4f()).rotationY((float)java.lang.Math.toRadians(90.0f));
		endLine.setLocalRotation(initialRotation);
		initialScale = (new Matrix4f()).scaling(0.2f);
		endLine.setLocalScale(initialScale);

		// add X,Y,-Z axes
		x = new GameObject(GameObject.root(), linxS);
		y = new GameObject(GameObject.root(), linyS);
		z = new GameObject(GameObject.root(), linzS);
		(x.getRenderStates()).setColor(new Vector3f(1f,0f,0f));
		(y.getRenderStates()).setColor(new Vector3f(0f,1f,0f));
		(z.getRenderStates()).setColor(new Vector3f(0f,0f,1f));

		// build terrain
		terr = new GameObject(GameObject.root(), terrS, hills);
		initialTranslation = (new Matrix4f()).translation(0f,-0.25f,0f);
		terr.setLocalTranslation(initialTranslation);
		initialScale = (new Matrix4f()).scaling(100.0f, 5.0f, 100.0f);
		terr.setLocalScale(initialScale);
		terr.setHeightMap(hillsMap);
		terr.getRenderStates().setTiling(1);
		terr.getRenderStates().setTileFactor(10);

		// build obstacles
		cone1 = new GameObject(GameObject.root(), coneShape, coneTexture);
		initialTranslation = (new Matrix4f()).translation(10.5f,0.15f,70.0f);
		cone1.setLocalTranslation(initialTranslation);
		initialScale = (new Matrix4f()).scaling(1.0f);
		cone1.setLocalScale(initialScale);

		cone2 = new GameObject(GameObject.root(), coneShape, coneTexture);
		initialTranslation = (new Matrix4f()).translation(10.5f,0.10f,45.0f);
		cone2.setLocalTranslation(initialTranslation);
		initialScale = (new Matrix4f()).scaling(1.0f);
		cone2.setLocalScale(initialScale);

		cone3 = new GameObject(GameObject.root(), coneShape, coneTexture);
		initialTranslation = (new Matrix4f()).translation(10.5f,0.0f,30.0f);
		cone3.setLocalTranslation(initialTranslation);
		initialScale = (new Matrix4f()).scaling(1.0f);
		cone3.setLocalScale(initialScale);

		cone4 = new GameObject(GameObject.root(), coneShape, coneTexture);
		initialTranslation = (new Matrix4f()).translation(10.5f,0.0f,15.0f);
		cone4.setLocalTranslation(initialTranslation);
		initialScale = (new Matrix4f()).scaling(1.0f);
		cone4.setLocalScale(initialScale);

		cone5 = new GameObject(GameObject.root(), coneShape, coneTexture);
		initialTranslation = (new Matrix4f()).translation(10.5f,0.0f,0.0f);
		cone5.setLocalTranslation(initialTranslation);
		initialScale = (new Matrix4f()).scaling(1.0f);
		cone5.setLocalScale(initialScale);

		cone6 = new GameObject(GameObject.root(), coneShape, coneTexture);
		initialTranslation = (new Matrix4f()).translation(10.5f,0.0f,-15.0f);
		cone6.setLocalTranslation(initialTranslation);
		initialScale = (new Matrix4f()).scaling(1.0f);
		cone6.setLocalScale(initialScale);

		cone7 = new GameObject(GameObject.root(), coneShape, coneTexture);
		initialTranslation = (new Matrix4f()).translation(10.5f,0.0f,-30.0f);
		cone7.setLocalTranslation(initialTranslation);
		initialScale = (new Matrix4f()).scaling(1.0f);
		cone7.setLocalScale(initialScale);

		cone8 = new GameObject(GameObject.root(), coneShape, coneTexture);
		initialTranslation = (new Matrix4f()).translation(10.5f,0.0f,-45.0f);
		cone8.setLocalTranslation(initialTranslation);
		initialScale = (new Matrix4f()).scaling(1.0f);
		cone8.setLocalScale(initialScale);

		cone9 = new GameObject(GameObject.root(), coneShape, coneTexture);
		initialTranslation = (new Matrix4f()).translation(10.5f,0.0f,-60.0f);
		cone9.setLocalTranslation(initialTranslation);
		initialScale = (new Matrix4f()).scaling(1.0f);
		cone9.setLocalScale(initialScale);

		cone10 = new GameObject(GameObject.root(), coneShape, coneTexture);
		initialTranslation = (new Matrix4f()).translation(10.5f,0.0f,-75.0f);
		cone10.setLocalTranslation(initialTranslation);
		initialScale = (new Matrix4f()).scaling(1.0f);
		cone10.setLocalScale(initialScale);
				
		//build audience
		audience = new GameObject(GameObject.root(), audienceS, audienceT);
	}

	@Override
	public void initializeLights()
	{	Light.setGlobalAmbient(.5f, .5f, .5f);

		light = new Light();
		light.setLocation(new Vector3f(0f, 5f, 0f));
		(engine.getSceneGraph()).addLight(light);

		playerLight = new Light();
		// playerLight.setAmbient(0.0f, 1.0f, 0.0f);
		playerLight.setDiffuse(0.0f, 1.0f, 0.0f);
		playerLight.setSpecular(0.0f, 1.0f, 0.0f);
		playerLight.setLinearAttenuation(0.5f);
		playerLight.setQuadraticAttenuation(0.1f);
		Vector3f direction = new Vector3f(0, -1, 0);
		playerLight.setDirection(direction);
		playerLight.setLocation(new Vector3f(avatar.getWorldLocation().x(), 2.0f, avatar.getWorldLocation().z()));
		(engine.getSceneGraph()).addLight(playerLight);

		ghostLight = new Light();
		ghostLight.setDiffuse(1.0f, 0.0f, 0.0f);
		ghostLight.setSpecular(1.0f, 0.0f, 0.0f);
		ghostLight.setLinearAttenuation(0.5f);
		ghostLight.setQuadraticAttenuation(0.1f);
		Vector3f ghostDirection = new Vector3f(0, -1, 0);
		ghostLight.setDirection(ghostDirection);
		ghostLight.setLocation(new Vector3f(avatar.getWorldLocation().x(), 2.0f, avatar.getWorldLocation().z()));
		(engine.getSceneGraph()).addLight(ghostLight);

	}

	@Override
	public void loadSounds() {
		AudioResource resource1, resource2, resource3, resource4, resource5;
		audioMgr = engine.getAudioManager();
		
		resource1 = audioMgr.createAudioResource("assets/sounds/engine.wav", AudioResourceType.AUDIO_SAMPLE);
		resource2 = audioMgr.createAudioResource("assets/sounds/bonk.wav", AudioResourceType.AUDIO_SAMPLE);
		resource3 = audioMgr.createAudioResource("assets/sounds/car.wav", AudioResourceType.AUDIO_SAMPLE);
		resource4 = audioMgr.createAudioResource("assets/sounds/honk.wav", AudioResourceType.AUDIO_SAMPLE);
		resource5 = audioMgr.createAudioResource("assets/sounds/bg.wav", AudioResourceType.AUDIO_SAMPLE);
		
		engineSound = new Sound(resource1, SoundType.SOUND_EFFECT, 100, true);
		oceanSound = new Sound(resource2, SoundType.SOUND_EFFECT, 100, false);
		carSound = new Sound(resource3, SoundType.SOUND_EFFECT, 0, false);
		honkSound = new Sound(resource4, SoundType.SOUND_EFFECT, 100, false);
		bgSound = new Sound(resource5, SoundType.SOUND_EFFECT, 75, true);

		engineSound.initialize(audioMgr);
		oceanSound.initialize(audioMgr);
		carSound.initialize(audioMgr);
		honkSound.initialize(audioMgr);
		bgSound.initialize(audioMgr);

		engineSound.setMaxDistance(10.0f);
		engineSound.setMinDistance(0.5f);
		engineSound.setRollOff(5.0f);
		oceanSound.setMaxDistance(1.0f);
		oceanSound.setMinDistance(0.1f);
		oceanSound.setRollOff(1.0f);
	}

	@Override
	public void initializeGame()
	{	prevTime = System.currentTimeMillis();
		startTime = System.currentTimeMillis();
		(engine.getRenderSystem()).setWindowDimensions(1900,1000);

		// ----------------- initialize camera ----------------
		positionCameraBehindAvatar();

		// ----------------- INPUTS SECTION -----------------------------
		im = engine.getInputManager();
		String gpName = im.getFirstGamepadName();

		Camera c = (engine.getRenderSystem()).getViewport("MAIN").getCamera();
		orbitController = new CameraOrbit3D(c, avatar, gpName, engine);

		// initialize physics system
		float[] gravity = {0.0f, -5.0f, 0.0f};
		physicsEngine = (engine.getSceneGraph().getPhysicsEngine());
		physicsEngine.setGravity(gravity);

		// create physics world
		float mass = 0.75f;
		float up[] = {0, 1, 0};
		float radius = 0.20f;
		float height = 0.7f;
		double[] tempTransform;

		Matrix4f rotationMatrix = new Matrix4f().rotateY((float) Math.toRadians(180));
		Matrix4f translation = new Matrix4f(avatar.getLocalTranslation());
		Matrix4f newTranslation = rotationMatrix.mulLocal(translation);
		tempTransform = toDoubleArray(newTranslation.get(vals));
		avt = (engine.getSceneGraph()).addPhysicsCapsuleX(mass, tempTransform, radius, height);
		avt.setBounciness(0.25f);
		avatar.setPhysicsObject(avt);

		rotationMatrix = new Matrix4f().rotateY((float) Math.toRadians(180));
		translation = new Matrix4f(carNew.getLocalTranslation());
		newTranslation = rotationMatrix.mulLocal(translation);
		tempTransform = toDoubleArray(newTranslation.get(vals));
		car = (engine.getSceneGraph()).addPhysicsCapsuleX(mass, tempTransform, radius, height);
		car.setBounciness(0.8f);
		carNew.setPhysicsObject(car);

		rotationMatrix = new Matrix4f().rotateY((float) Math.toRadians(180));
		translation = new Matrix4f(cone1.getLocalTranslation());
		newTranslation = rotationMatrix.mulLocal(translation);
		tempTransform = toDoubleArray(newTranslation.get(vals));
		cone1P = (engine.getSceneGraph()).addPhysicsCone(mass, tempTransform, radius, height);
		cone1P.setBounciness(0.8f);
		cone1.setPhysicsObject(cone1P);

		rotationMatrix = new Matrix4f().rotateY((float) Math.toRadians(180));
		translation = new Matrix4f(cone2.getLocalTranslation());
		newTranslation = rotationMatrix.mulLocal(translation);
		tempTransform = toDoubleArray(newTranslation.get(vals));
		cone2P = (engine.getSceneGraph()).addPhysicsCone(mass, tempTransform, radius, height);
		cone2P.setBounciness(0.8f);
		cone2.setPhysicsObject(cone2P);

		rotationMatrix = new Matrix4f().rotateY((float) Math.toRadians(180));
		translation = new Matrix4f(cone3.getLocalTranslation());
		newTranslation = rotationMatrix.mulLocal(translation);
		tempTransform = toDoubleArray(newTranslation.get(vals));
		cone3P = (engine.getSceneGraph()).addPhysicsCone(mass, tempTransform, radius, height);
		cone3P.setBounciness(0.8f);
		cone3.setPhysicsObject(cone3P);

		rotationMatrix = new Matrix4f().rotateY((float) Math.toRadians(180));
		translation = new Matrix4f(cone4.getLocalTranslation());
		newTranslation = rotationMatrix.mulLocal(translation);
		tempTransform = toDoubleArray(newTranslation.get(vals));
		cone4P = (engine.getSceneGraph()).addPhysicsCone(mass, tempTransform, radius, height);
		cone4P.setBounciness(0.8f);
		cone4.setPhysicsObject(cone4P);

		rotationMatrix = new Matrix4f().rotateY((float) Math.toRadians(180));
		translation = new Matrix4f(cone5.getLocalTranslation());
		newTranslation = rotationMatrix.mulLocal(translation);
		tempTransform = toDoubleArray(newTranslation.get(vals));
		cone5P = (engine.getSceneGraph()).addPhysicsCone(mass, tempTransform, radius, height);
		cone5P.setBounciness(0.8f);
		cone5.setPhysicsObject(cone5P);

		rotationMatrix = new Matrix4f().rotateY((float) Math.toRadians(180));
		translation = new Matrix4f(cone6.getLocalTranslation());
		newTranslation = rotationMatrix.mulLocal(translation);
		tempTransform = toDoubleArray(newTranslation.get(vals));
		cone6P = (engine.getSceneGraph()).addPhysicsCone(mass, tempTransform, radius, height);
		cone6P.setBounciness(0.8f);
		cone6.setPhysicsObject(cone6P);

		rotationMatrix = new Matrix4f().rotateY((float) Math.toRadians(180));
		translation = new Matrix4f(cone7.getLocalTranslation());
		newTranslation = rotationMatrix.mulLocal(translation);
		tempTransform = toDoubleArray(newTranslation.get(vals));
		cone7P = (engine.getSceneGraph()).addPhysicsCone(mass, tempTransform, radius, height);
		cone7P.setBounciness(0.8f);
		cone7.setPhysicsObject(cone7P);

		rotationMatrix = new Matrix4f().rotateY((float) Math.toRadians(180));
		translation = new Matrix4f(cone8.getLocalTranslation());
		newTranslation = rotationMatrix.mulLocal(translation);
		tempTransform = toDoubleArray(newTranslation.get(vals));
		cone8P = (engine.getSceneGraph()).addPhysicsCone(mass, tempTransform, radius, height);
		cone8P.setBounciness(0.8f);
		cone8.setPhysicsObject(cone8P);

		rotationMatrix = new Matrix4f().rotateY((float) Math.toRadians(180));
		translation = new Matrix4f(cone9.getLocalTranslation());
		newTranslation = rotationMatrix.mulLocal(translation);
		tempTransform = toDoubleArray(newTranslation.get(vals));
		cone9P = (engine.getSceneGraph()).addPhysicsCone(mass, tempTransform, radius, height);
		cone9P.setBounciness(0.8f);
		cone9.setPhysicsObject(cone9P);

		rotationMatrix = new Matrix4f().rotateY((float) Math.toRadians(180));
		translation = new Matrix4f(cone10.getLocalTranslation());
		newTranslation = rotationMatrix.mulLocal(translation);
		tempTransform = toDoubleArray(newTranslation.get(vals));
		cone10P = (engine.getSceneGraph()).addPhysicsCone(mass, tempTransform, radius, height);
		cone10P.setBounciness(0.8f);
		cone10.setPhysicsObject(cone10P);
		
		translation = new Matrix4f(terr.getLocalTranslation());
		tempTransform = toDoubleArray(translation.get(vals));
		planeP = (engine.getSceneGraph()).addPhysicsStaticPlane(tempTransform, up, 0.0f);
		planeP.setBounciness(1.0f);
		terr.setPhysicsObject(planeP);

		engine.enableGraphicsWorldRender();
		// engine.enablePhysicsWorldRender();

		// build some action objects for doing things in response to user input
		Vector3f fwdForce = new Vector3f(0.0f, 0.0f, -2.5f); // Apply force in the negative Z direction
		Vector3f bwdForce = new Vector3f(0.0f, 0.0f, 2.5f);

		Vector3f position = new Vector3f(0.0f, 0.0f, 0.0f); // Apply force at the center of the object
		
		FwdAction fwdAction = new FwdAction(avt, fwdForce, position);
		FwdAction bwdAction = new FwdAction(avt, bwdForce, position);

		Vector3f leftTorque = new Vector3f(0.0f, 2.0f, 0.0f); // Apply torque around the Y-axis
		Vector3f rightTorque = new Vector3f(0.0f, -2.0f, 0.0f);
		
		TurnAction leftTurnAction = new TurnAction(avt, leftTorque);
		TurnAction rightTurnAction = new TurnAction(avt, rightTorque);

		// attach the action objects to keyboard and gamepad components
		im.associateActionWithAllKeyboards(
			net.java.games.input.Component.Identifier.Key.UP,
			fwdAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllKeyboards(
			net.java.games.input.Component.Identifier.Key.DOWN,
			bwdAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllKeyboards(
			net.java.games.input.Component.Identifier.Key.LEFT,
			leftTurnAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllKeyboards(
			net.java.games.input.Component.Identifier.Key.RIGHT,
			rightTurnAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

		im.associateActionWithAllGamepads(
			net.java.games.input.Component.Identifier.Axis.RZ,
			fwdAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllGamepads(
			net.java.games.input.Component.Identifier.Axis.Z,
			bwdAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllGamepads(
			net.java.games.input.Component.Identifier.Axis.X,
			leftTurnAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllGamepads(
			net.java.games.input.Component.Identifier.Axis.X,
			rightTurnAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

		// initialize sound settings
		engineSound.setLocation(carNew.getWorldLocation());
		oceanSound.setLocation(carNew.getWorldLocation());
		carSound.setLocation(avatar.getWorldLocation());
		honkSound.setLocation(avatar.getWorldLocation());

		setEarParameters();

		engineSound.play();
		bgSound.play();

		// oceanSound.play();

		setupNetworking();
	}

	public GameObject getAvatar() { return avatar; }
	public ObjShape getAvatarShape() { return dolS; }
	public TextureImage getAvatarTexture() { return doltx; }
	public ObjShape getNPCshape() { return npcShape; }
	public TextureImage getNPCtexture() { return npcTex; }
	public ObjShape getNPC2shape() { return npcShape2; }
	public TextureImage getNPC2texture() { return npcTex2; }

	public Sound getHonk() { return honkSound; }

	public void selectAnAvatar() {
		positionCameraBehindAvatar();
		if (selectAvatar) {
			float height = terr.getHeight(avatar.getWorldLocation().x(), avatar.getWorldLocation().z());
			initialTranslation = (new Matrix4f()).translation(11.5f,height,80.0f);
			avatar.setLocalTranslation(initialTranslation);
			initialRotation = (new Matrix4f()).rotationY((float)java.lang.Math.toRadians(180.0f));
			avatar.setLocalRotation(initialRotation);
			initialScale = (new Matrix4f()).scaling(0.20f);
			avatar.setLocalScale(initialScale);

			if (finish) {
				if (winS) {
					String rematch = "Rematch?";
					Vector3f hud1Color = new Vector3f(1,0,0);
					(engine.getHUDmanager()).setHUD1("Time: " + time + " " + rematch, hud1Color, 860, 500);
				} else if (winM) {
					String rematch = "Rematch?";
					String place = "You placed 1st";
					Vector3f hud1Color = new Vector3f(1,0,0);
					(engine.getHUDmanager()).setHUD1(rematch, hud1Color, 860, 500);

					(engine.getHUDmanager()).setHUD2(place, hud1Color, 860, 520);
				} else if (!winM) {
					String rematch = "Rematch?";
					String place = "You placed 2nd";
					Vector3f hud1Color = new Vector3f(1,0,0);
					(engine.getHUDmanager()).setHUD1(rematch, hud1Color, 860, 500);

					(engine.getHUDmanager()).setHUD2(place, hud1Color, 860, 520);
				} else {
					String rematch = "";
					Vector3f hud1Color = new Vector3f(1,0,0);
					(engine.getHUDmanager()).setHUD1(rematch, hud1Color, 860, 500);
				}

				String select = "Select an Avatar";
				Vector3f hud2Color = new Vector3f(1,0,0);
				(engine.getHUDmanager()).setHUD2(select, hud2Color, 860, 100);
			} else {
				String select = "Select an Avatar";
				Vector3f hud1Color = new Vector3f(1,0,0);
				(engine.getHUDmanager()).setHUD1(select, hud1Color, 860, 100);
			}
		}
	}

	@Override
	public void update()
	{	
		// update avatar
		selectAnAvatar();

		elapsedTime = System.currentTimeMillis() - prevTime;
		prevTime = System.currentTimeMillis();
		amt = elapsedTime * 0.03;
		Camera c = (engine.getRenderSystem()).getViewport("MAIN").getCamera();
		
		if (selectAvatar == false) {	
			// update physics
			if (running) {
				AxisAngle4f aa = new AxisAngle4f();
				Matrix4f mat = new Matrix4f();
				Matrix4f mat2 = new Matrix4f().identity();
				Matrix4f mat3 = new Matrix4f().identity();
				checkForCollisions();
				physicsEngine.update((float) elapsedTime);
				for (GameObject go:engine.getSceneGraph().getGameObjects()) {
					if (go.getPhysicsObject() != null) {
						// set translation
						mat.set(toFloatArray(go.getPhysicsObject().getTransform()));
						Vector3f newTranslation = new Vector3f(mat.m30(), mat.m31(), mat.m32());
						Vector3f currentTranslation = new Vector3f();
						go.getLocalTranslation().getTranslation(currentTranslation);
						Vector3f smoothTranslation = new Vector3f(currentTranslation);
						smoothTranslation.lerp(newTranslation, 0.1f); // interpolate between current and new translation
						mat2.setTranslation(smoothTranslation);
						go.setLocalTranslation(mat2);
						
						// set rotation
						mat.getRotation(aa);
						Quaternionf newRotation = new Quaternionf().set(aa);
						Quaternionf currentRotation = new Quaternionf();
						go.getLocalRotation().getUnnormalizedRotation(currentRotation);
						
						// Only keep the y-axis rotation
						newRotation.x = 0;
						newRotation.z = 0;
						currentRotation.x = 0;
						currentRotation.z = 0;
						
						Quaternionf smoothRotation = new Quaternionf(currentRotation);
						smoothRotation.slerp(newRotation, 0.1f); // interpolate between current and new rotation
						mat3.rotation(smoothRotation);
						go.setLocalRotation(mat3);
					}
				}
			}

			engineSound.setLocation(carNew.getWorldLocation());
			bgSound.setLocation(avatar.getWorldLocation());

			// update sound
			if (engineSound.getLocation() != avatar.getWorldLocation()) {
				float distance = 4 * engineSound.getLocation().distance(avatar.getWorldLocation());
				if (distance < 100.0f) {
					engineSound.setVolume(100 - (int)distance);
				} else {
					engineSound.setVolume(0);
				}
			}
			setEarParameters();

			// update sound
			oceanSound.setLocation(carNew.getWorldLocation());
			if (avatar.getWorldLocation().distance(carNew.getWorldLocation()) < 1.0f) {
				oceanSound.play();
			}
			
			Vector3f currentPos = avatar.getWorldLocation();
			if (!currentPos.equals(previousPosition)) {
				carSound.setLocation(currentPos);
				if (!carSound.getIsPlaying()) {
					carSound.play();
				}
			} else {
				carSound.stop();
			}

			previousPosition = currentPos;

			// build and set HUD
			int elapsTimeSec = Math.round((float)(System.currentTimeMillis()-startTime)/1000.0f);
			String elapsTimeStr = Integer.toString(elapsTimeSec);
			String counterStr = Integer.toString(counter);
			String dispStr1 = "Time = " + elapsTimeStr;
			// String dispStr2 = "camera position = "
			// 	+ (avatar.getWorldLocation()).x()
			// 	+ ", " + (avatar.getWorldLocation()).y()
			// 	+ ", " + (avatar.getWorldLocation()).z();
			String dispStr2 = "Score = " + score;
			Vector3f hud1Color = new Vector3f(1,0,0);
			Vector3f hud2Color = new Vector3f(1,1,1);
			(engine.getHUDmanager()).setHUD1(dispStr1, hud1Color, 15, 15);
			(engine.getHUDmanager()).setHUD2(dispStr2, hud2Color, 500, 15);

			// update inputs and camera
			im.update((float)elapsedTime);
			positionCameraBehindAvatar();
			processNetworking((float)elapsedTime);

			// update avatar altitude
			Vector3f loc = avatar.getWorldLocation();
			Vector3f loc2 = carNew.getWorldLocation();
			float height = terr.getHeight(loc.x(), loc.z());
			avatar.setLocalLocation(new Vector3f(loc.x(), height, loc.z()));
			carNew.setLocalLocation(new Vector3f(loc2.x(), height, loc2.z()));


			//update audience
			audienceS.updateAnimation();

			// update camera
			orbitController.updateCameraPosition();

			// update Lights
			playerLight.setLocation(new Vector3f(avatar.getWorldLocation().x(), 2.0f, avatar.getWorldLocation().z()));
			if (ghostID != null) {
				ghostLight.setLocation(new Vector3f(gm.getGhostAvatarPosition(ghostID).x(), 2.0f, gm.getGhostAvatarPosition(ghostID).z()));
			}

			// update obstacles
			float terrainHeight = terr.getHeight(cone1.getWorldLocation().x(), cone1.getWorldLocation().z());
			cone1.setLocalLocation(new Vector3f(cone1.getWorldLocation().x(), terrainHeight, cone1.getWorldLocation().z()));

			terrainHeight = terr.getHeight(cone2.getWorldLocation().x(), cone2.getWorldLocation().z());
			cone2.setLocalLocation(new Vector3f(cone2.getWorldLocation().x(), terrainHeight, cone2.getWorldLocation().z()));

			terrainHeight = terr.getHeight(cone3.getWorldLocation().x(), cone3.getWorldLocation().z());
			cone3.setLocalLocation(new Vector3f(cone3.getWorldLocation().x(), terrainHeight, cone3.getWorldLocation().z()));

			terrainHeight = terr.getHeight(cone4.getWorldLocation().x(), cone4.getWorldLocation().z());
			cone4.setLocalLocation(new Vector3f(cone4.getWorldLocation().x(), terrainHeight, cone4.getWorldLocation().z()));

			terrainHeight = terr.getHeight(cone5.getWorldLocation().x(), cone5.getWorldLocation().z());
			cone5.setLocalLocation(new Vector3f(cone5.getWorldLocation().x(), terrainHeight, cone5.getWorldLocation().z()));

			terrainHeight = terr.getHeight(cone6.getWorldLocation().x(), cone6.getWorldLocation().z());
			cone6.setLocalLocation(new Vector3f(cone6.getWorldLocation().x(), terrainHeight, cone6.getWorldLocation().z()));

			terrainHeight = terr.getHeight(cone7.getWorldLocation().x(), cone7.getWorldLocation().z());
			cone7.setLocalLocation(new Vector3f(cone7.getWorldLocation().x(), terrainHeight, cone7.getWorldLocation().z()));

			terrainHeight = terr.getHeight(cone8.getWorldLocation().x(), cone8.getWorldLocation().z());
			cone8.setLocalLocation(new Vector3f(cone8.getWorldLocation().x(), terrainHeight, cone8.getWorldLocation().z()));

			terrainHeight = terr.getHeight(cone9.getWorldLocation().x(), cone9.getWorldLocation().z());
			cone9.setLocalLocation(new Vector3f(cone9.getWorldLocation().x(), terrainHeight, cone9.getWorldLocation().z()));

			terrainHeight = terr.getHeight(cone10.getWorldLocation().x(), cone10.getWorldLocation().z());
			cone10.setLocalLocation(new Vector3f(cone10.getWorldLocation().x(), terrainHeight, cone10.getWorldLocation().z()));
			
			// check winner
			if (avatar.getWorldLocation().z() < -75.0f) {
				if (ghostID != null) {
					if (gm.getGhostAvatarPosition(ghostID).z() > avatar.getWorldLocation().z()) {
						winM = true;
					} else {
						winM = false;
					}
					finish = true;
					selectAvatar = true;
				} else {
					winS = true;
					finish = true;
					selectAvatar = true;
					time = String.valueOf(elapsedTime);
				}
			}
		}
	}

	public void setEarParameters() {
		Camera camera = (engine.getRenderSystem()).getViewport("MAIN").getCamera();
		audioMgr.getEar().setLocation(avatar.getWorldLocation());
		audioMgr.getEar().setOrientation(camera.getN(), new Vector3f(0.0f, 1.0f, 0.0f));
	}

	private void positionCameraBehindAvatar()
	{	Vector4f u = new Vector4f(-1f,0f,0f,1f);
		Vector4f v = new Vector4f(0f,1f,0f,1f);
		Vector4f n = new Vector4f(0f,0f,1f,1f);
		u.mul(avatar.getWorldRotation());
		v.mul(avatar.getWorldRotation());
		n.mul(avatar.getWorldRotation());
		Matrix4f w = avatar.getWorldTranslation();
		Vector3f position = new Vector3f(w.m30(), w.m31(), w.m32());
		position.add(-n.x()*2f, -n.y()*2f, -n.z()*2f);
		position.add(v.x()*.75f, v.y()*.75f, v.z()*.75f);
		Camera c = (engine.getRenderSystem()).getViewport("MAIN").getCamera();
		c.setLocation(position);
		c.setU(new Vector3f(u.x(),u.y(),u.z()));
		c.setV(new Vector3f(v.x(),v.y(),v.z()));
		c.setN(new Vector3f(n.x(),n.y(),n.z()));
	}

	public class FwdAction extends AbstractInputAction {
		private PhysicsObject avatarPhysics;
		private Vector3f force;
		private Vector3f position;
	
		public FwdAction(PhysicsObject avatarPhysics, Vector3f force, Vector3f position) {
			this.avatarPhysics = avatarPhysics;
			this.force = force;
			this.position = position;
		}
	
		public void performAction(float time, Event event) {
			avatarPhysics.applyForce(force.x, force.y, force.z, position.x, position.y, position.z);
		}
	}
	
	public class TurnAction extends AbstractInputAction {
		private PhysicsObject avatarPhysics;
		private Vector3f torque;
	
		public TurnAction(PhysicsObject avatarPhysics, Vector3f torque) {
			this.avatarPhysics = avatarPhysics;
			this.torque = torque;
		}
	
		public void performAction(float time, Event event) {
			avatarPhysics.applyTorque(torque.x, torque.y, torque.z);
		}
	}

	@Override
	public void keyPressed(KeyEvent e)
	{	
		if (!selectAvatar) {
			switch (e.getKeyCode())
			{	case KeyEvent.VK_W:
				{	Vector3f oldPosition = avatar.getWorldLocation();
					Vector4f fwdDirection = new Vector4f(0f,0f,1f,1f);
					fwdDirection.mul(avatar.getWorldRotation());
					fwdDirection.mul(0.25f);
					Vector3f newPosition = oldPosition.add(fwdDirection.x(), fwdDirection.y(), fwdDirection.z());
					avatar.setLocalLocation(newPosition);
					protClient.sendMoveMessage(avatar.getWorldLocation());
					break;
				}
				case KeyEvent.VK_S:
				{	Vector3f oldPosition = avatar.getWorldLocation();
					Vector4f fwdDirection = new Vector4f(0f,0f,1f,1f);
					fwdDirection.mul(avatar.getWorldRotation());
					fwdDirection.mul(-0.25f);
					Vector3f newPosition = oldPosition.add(fwdDirection.x(), fwdDirection.y(), fwdDirection.z());
					avatar.setLocalLocation(newPosition);
					protClient.sendMoveMessage(avatar.getWorldLocation());
					break;
				}
				case KeyEvent.VK_A:
				{	Matrix4f oldRotation = new Matrix4f(avatar.getWorldRotation());
					Vector4f oldUp = new Vector4f(0f,1f,0f,1f).mul(oldRotation);
					Matrix4f rotAroundAvatarUp = new Matrix4f().rotation(0.1f, new Vector3f(oldUp.x(), oldUp.y(), oldUp.z()));
					Matrix4f newRotation = oldRotation;
					newRotation.mul(rotAroundAvatarUp);
					avatar.setLocalRotation(newRotation);
					break;
				}
				case KeyEvent.VK_D:
				{	Matrix4f oldRotation = new Matrix4f(avatar.getWorldRotation());
					Vector4f oldUp = new Vector4f(0f,1f,0f,1f).mul(oldRotation);
					Matrix4f rotAroundAvatarUp = new Matrix4f().rotation(-.1f, new Vector3f(oldUp.x(), oldUp.y(), oldUp.z()));
					Matrix4f newRotation = oldRotation;
					newRotation.mul(rotAroundAvatarUp);
					avatar.setLocalRotation(newRotation);
					break;
				}
				case KeyEvent.VK_SPACE:
				{	System.out.println("starting physics");
					running = !running;
					break;
				}
				case KeyEvent.VK_1:
				{
					audienceS.stopAnimation();
					audienceS.playAnimation("jumping", 0.5f, AnimatedShape.EndType.LOOP, 0);
					break;
				}
				case KeyEvent.VK_2:
				{
					audienceS.stopAnimation();
					audienceS.playAnimation("clap", 0.5f, AnimatedShape.EndType.LOOP, 0);
					break;
				}
				case KeyEvent.VK_0:
				{
					audienceS.stopAnimation();
					break;
				}
			}
		} else {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_1:
				{
					avatar.setShape(dolS);
					avatar.setTextureImage(doltx);
					avatarName = "";
					avatarName = "dol";
					update();
					break;					
				}
				case KeyEvent.VK_2:
				{
					avatar.setShape(ghostS);
					avatar.setTextureImage(ghostT);
					Matrix4f rotate = (new Matrix4f()).rotationY((float)java.lang.Math.toRadians(180.0f));
					carNew.setLocalRotation(rotate);
					avatarName = "";
					avatarName = "ghost";
					update();
					break;
				}
				case KeyEvent.VK_3:
				{
					selectAvatar = false;
					update();
					break;
				}
			}
		}
		super.keyPressed(e);
	}

	// ------------------ UTILITY FUNCTIONS used by physics
	private float[] toFloatArray(double[] arr) {
		if (arr == null) return null;
		int n = arr.length;
		float[] ret = new float[n];
		for (int i = 0; i < n; i++) {
			ret[i] = (float)arr[i];
		}
		return ret;
	}

	private double[] toDoubleArray(float[] arr) {
		if (arr == null) return null;
		int n = arr.length;
		double[] ret = new double[n];
		for (int i = 0; i < n; i++) {
			ret[i] = (double)arr[i];
		}
		return ret;
	}

	private void checkForCollisions() {
		com.bulletphysics.dynamics.DynamicsWorld dynamicsWorld;
		com.bulletphysics.collision.broadphase.Dispatcher dispatcher;
		com.bulletphysics.collision.narrowphase.PersistentManifold manifold;
		com.bulletphysics.dynamics.RigidBody object1, object2;
		com.bulletphysics.collision.narrowphase.ManifoldPoint contactPoint;
		dynamicsWorld = ((JBulletPhysicsEngine)physicsEngine).getDynamicsWorld();
		dispatcher = dynamicsWorld.getDispatcher();
		int manifoldCount = dispatcher.getNumManifolds();

		for (int i=0; i<manifoldCount; i++) {
			manifold = dispatcher.getManifoldByIndexInternal(i);
			object1 = (com.bulletphysics.dynamics.RigidBody)manifold.getBody0();
			object2 = (com.bulletphysics.dynamics.RigidBody)manifold.getBody1();
			JBulletPhysicsObject obj1 = JBulletPhysicsObject.getJBulletPhysicsObject(object1);
			JBulletPhysicsObject obj2 = JBulletPhysicsObject.getJBulletPhysicsObject(object2);
			for (int j = 0; j < manifold.getNumContacts(); j++) {
				contactPoint = manifold.getContactPoint(j);
				if (contactPoint.getDistance() < 0.0f) {
					System.out.println("---- hit between " + obj1 + " and " + obj2);
					break;
				}
			}
		}
	}

	// ---------- NETWORKING SECTION ----------------

	public ObjShape getGhostShape() { return ghostS; }
	public TextureImage getGhostTexture() { return ghostT; }
	public GhostManager getGhostManager() { return gm; }
	public Engine getEngine() { return engine; }
	
	private void setupNetworking()
	{	isClientConnected = false;	
		try 
		{	protClient = new ProtocolClient(InetAddress.getByName(serverAddress), serverPort, serverProtocol, this);
		} 	catch (UnknownHostException e) 
		{	e.printStackTrace();
		}	catch (IOException e) 
		{	e.printStackTrace();
		}
		if (protClient == null)
		{	System.out.println("missing protocol host");
		}
		else
		{	// Send the initial join message with a unique identifier for this client
			System.out.println("sending join message to protocol host");
			protClient.sendJoinMessage();
		}
	}
	
	protected void processNetworking(float elapsTime)
	{	// Process packets received by the client from the server
		if (protClient != null)
			protClient.processPackets();
	}

	public Vector3f getPlayerPosition() { return avatar.getWorldLocation(); }

	public void setIsConnected(boolean value) { this.isClientConnected = value; }
	
	private class SendCloseConnectionPacketAction extends AbstractInputAction
	{	@Override
		public void performAction(float time, net.java.games.input.Event evt) 
		{	if(protClient != null && isClientConnected == true)
			{	protClient.sendByeMessage();
			}
		}
	}

	public String getAvatarName() { return avatarName; }

	public int getScore() { return score; }

	public void setScore(int score) { this.score = score; }

	public void setUUID(UUID id) { this.ghostID = id; }

	public GameObject getTerrain() {
		return terr;
	}

	public void setStart(boolean finish) {
		selectAvatar = false;
		update();
	}
}

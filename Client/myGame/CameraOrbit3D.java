package myGame;

import tage.*;
import tage.input.InputManager;
import tage.input.action.*;
import net.java.games.input.*;
import org.joml.*;

import com.jogamp.opengl.awt.GLCanvas;

import java.lang.Math;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class CameraOrbit3D implements MouseMotionListener, MouseWheelListener {
    private Engine engine;
    private Camera camera;
    private GameObject avatar;

    private float cameraAzimuth;
    private float cameraElevation;
    private float cameraRadius;

    private static final float MOUSE_SENSITIVITY = 0.1f;
    private static final float WHEEL_SENSITIVITY = 0.1f;

    private float previousMouseX = 0;
    private float previousMouseY = 0;

    public CameraOrbit3D(Camera cam, GameObject av, String gpName, Engine e) {
        engine = e;
        camera = cam;
        avatar = av;
        cameraAzimuth = 0.0f;
        cameraElevation = 20.0f;
        cameraRadius = 2.0f;
        
        RenderSystem renderSystem = engine.getRenderSystem();
        GLCanvas component = renderSystem.getGLCanvas(); // Replace getCanvas() with the actual method to get the component
        component.addMouseMotionListener(this);
        component.addMouseWheelListener(this);

        setupInputs(gpName);
        updateCameraPosition();
    }

    private void setupInputs(String gp) {
        OrbitAzimuthAction azmAction = new OrbitAzimuthAction();
        OrbitElevationAction elevAction = new OrbitElevationAction();
        OrbitRadiusAction radAction = new OrbitRadiusAction();

        InputManager im = engine.getInputManager();

        if (gp != null) {
            im.associateAction(gp,
                net.java.games.input.Component.Identifier.Axis.RX, azmAction,
                InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

            im.associateAction(gp,
                net.java.games.input.Component.Identifier.Axis.RY, elevAction,
                InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

            im.associateAction(gp,
                net.java.games.input.Component.Identifier.Button._1, radAction,
                InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

            
            im.associateAction(gp,
                net.java.games.input.Component.Identifier.Button._2, radAction,
                InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        }
    }

    public void updateCameraPosition() {
        Vector3f avatarRot = avatar.getWorldForwardVector();
        double avatarAngle = Math.toDegrees((double) avatarRot.angleSigned(new Vector3f(0, 0, -1), new Vector3f(0, 1, 0)));
        float totalAz = cameraAzimuth - (float) avatarAngle;
        double theta = Math.toRadians(totalAz);
        double phi = Math.toRadians(cameraElevation);
        float x = cameraRadius * (float) (Math.cos(phi) * Math.sin(theta));
        float y = cameraRadius * (float) (Math.sin(phi));
        float z = cameraRadius * (float) (Math.cos(phi) * Math.cos(theta));
        camera.setLocation(new Vector3f(x, y, z).add(avatar.getWorldLocation()));
        camera.lookAt(avatar);
    }

    private class OrbitAzimuthAction extends AbstractInputAction {
        @Override
        public void performAction(float time, Event event) {
            float rotAmount;

            if (event.getValue() < -0.2) {
                rotAmount = -0.75f;
            } else {
                if (event.getValue() > 0.2) {
                    rotAmount = 0.75f;
                } else {
                    rotAmount = 0.0f;
                }
            }
            cameraAzimuth += rotAmount;
            cameraAzimuth = cameraAzimuth % 360;
            updateCameraPosition();
        }
    }

    private class OrbitRadiusAction extends AbstractInputAction {
        @Override
        public void performAction(float time, Event event) {
            float distAmount;

            // Assuming LT (Left Trigger) is pressed for zooming out
            if (event.getComponent().getIdentifier() == Component.Identifier.Button._1) {
                distAmount = -0.1f;
            }
            // Assuming RT (Right Trigger) is pressed for zooming in
            else {
                if (event.getComponent().getIdentifier() == Component.Identifier.Button._2) {
                    distAmount = 0.1f;
                } else {
                    distAmount = 0.0f;
                }
            }

            cameraRadius += distAmount;
            if (cameraRadius < 0.5f) {
                cameraRadius = 0.5f;
            }

            updateCameraPosition();
        }
    }


    private class OrbitElevationAction extends AbstractInputAction {
        @Override
        public void performAction(float time, Event event) {
            float elevAmount;

            if (event.getValue() < -0.2) {
                elevAmount = -0.75f;
            } else if (event.getValue() > 0.2) {
                elevAmount = 0.75f;
            } else {
                elevAmount = 0.0f;
            }

            cameraElevation += elevAmount;

            if (cameraElevation < 0.0f) {
                cameraElevation = 0.0f;
            } else if (cameraElevation > 90.0f) {
                cameraElevation = 90.0f;
            }

            updateCameraPosition();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // Calculate the change in mouse position
        float dx = previousMouseX - e.getX();
        float dy = e.getY() - previousMouseY;

        // Update the camera azimuth and elevation based on mouse movement
        cameraAzimuth += dx * MOUSE_SENSITIVITY;
        cameraElevation += dy * MOUSE_SENSITIVITY;

        // Clamp the camera elevation to [-90, 90] degrees
        cameraElevation = Math.max(-90, Math.min(90, cameraElevation));

        // Update the previous mouse position
        previousMouseX = e.getX();
        previousMouseY = e.getY();

        // Update the camera position
        updateCameraPosition();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // Do nothing
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        // Update the camera radius based on mouse wheel movement
        cameraRadius += e.getWheelRotation() * WHEEL_SENSITIVITY;

        // Clamp the camera radius to a positive value
        cameraRadius = Math.max(0, cameraRadius);

        // Update the camera position
        updateCameraPosition();
    }
}

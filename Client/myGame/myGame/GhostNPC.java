package myGame;

import java.lang.Math;
import org.joml.*;

import tage.*;

public class GhostNPC extends GameObject {
    private int id;

    public GhostNPC(int id, ObjShape s, TextureImage t, Vector3f p) {
        super(GameObject.root(), s, t);
        this.id = id;
        setPosition(p);
        setSize(0.20f);
    }

    public void setSize(float size) {
        this.setLocalScale((new Matrix4f()).scaling(size));
    }

    public void setPosition(Vector3f p) {
        this.setLocalTranslation(new Matrix4f().translation(p));
    }

    public void setRotation(double rotation) {
        Matrix4f newRotation = (new Matrix4f()).rotationY((float)java.lang.Math.toRadians(rotation));
		this.setLocalRotation(newRotation);
    }

    public Matrix4f getPosition() {
        return this.getLocalTranslation();
    }
}

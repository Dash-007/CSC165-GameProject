public class NPC {
    double locationX, locationY, locationZ;
    double dir = 0.1;
    double size = 1.0;
    double rotation = 180.0;

    public NPC() {
        locationX = 11.5;
        locationY = 0.1;
        locationZ = 3.0;
    }

    // public void randomizeLocation(int seedX, int seedZ) {
    //     locationX = 2.5;
    //     locationY = 0.5;
    //     locationZ = -2.0;
    // }

    public double getX() {
        return locationX;
    }

    public double getY() {
        return locationY;
    }

    public double getZ() {
        return locationZ;
    }

    public void getBig() {
        size = 20.0;
    }

    public void getSmall() {
        size = 10.0;
    }

    public double getSize() {
        return size;
    }

    public double getRotation() {
        return rotation;
    }

    public void updateLocation() {
        if (locationZ > 05) {
            dir = -0.1;
            rotation -= 180.0; // Rotate in the opposite direction
            if (rotation < 0.0) { // Keep rotation within [0, 360)
                rotation += 360.0;
            }
        } else if (locationZ < -20) {
            dir = 0.1;
            rotation += 180.0; // Rotate in the opposite direction
            if (rotation > 360.0) {
                rotation -= 360.0;
            }
        }
        locationZ += dir;
    }
}

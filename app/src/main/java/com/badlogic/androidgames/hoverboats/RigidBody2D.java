package com.badlogic.androidgames.hoverboats;

import framework.classes.DynamicGameObject;
import framework.classes.Vector2;
import framework.classes.Vector3;

/**
 * Created by New PC 2012 on 12/01/2015.
 */
public class RigidBody2D {
    float fMass; // total mass (constant)
    float fInertia; // mass moment of inertia
    float fInertiaInverse; // inverse of mass moment of inertia
    Vector3 vPosition; // position in earth coordinates
    Vector3 vVelocity; // velocity in earth coordinates
    Vector3 vVelocityBody; // velocity in body coordinates
    Vector3 vAngularVelocity; // angular velocity in body coordinates
    float fSpeed; // speed
    float fOrientation; // orientation
    Vector3 vForces; // total force on body
    Vector3 vMoment; // total moment on body
    float ThrustForce; // Magnitude of the thrust force
    Vector3 PThrust, SThrust; // bow thruster forces
    float fWidth; // bounding dimensions
    float fLength;
    float fHeight;
    float rho = 1.2475f;
    float tol = 0.1f;
    Vector3 CD; // location of center of drag in body coordinates
    Vector3 CT; // location of center of propeller thrust in body coords.
    Vector3 CPT; // location of port bow thruster thrust in body coords.
    Vector3 CST; // location of starboard bow thruster thrust in body
    // coords.
    float ProjectedArea; // projected area of the body
//    RigidBody2D(void);
//    void CalcLoads(void);
//    void UpdateBodyEuler(double dt);
//    void SetThrusters(bool p, bool s);
//    void ModulateThrust(bool up);


    public RigidBody2D(float x, float y, float width, float length) {
        fMass = 100;
        fInertia = fMass*(length*length+width*width)/12f;
        fInertiaInverse = 1/fInertia;
        vPosition.x = x;
        vPosition.y = y;

        fWidth = width;
        fLength = length;
        fHeight = 5;
        fOrientation = 0;
        CD.x = -0.25f*fLength;
        CD.y = 0.0f;
        CD.z = 0.0f;
        CT.x = -0.5f*fLength;
        CT.y = 0.0f;
        CT.z = 0.0f;
        CPT.x = 0.5f*fLength;
        CPT.y = -0.5f*fWidth;
        CPT.z = 0.0f;
        CST.x = 0.5f*fLength;
        CST.y = 0.5f*fWidth;
        CST.z = 0.0f;
        ProjectedArea = (fLength + fWidth)/2 * fHeight; // an approximation
        ThrustForce = 500f;
    }

    public RigidBody2D(){
        fMass = 100;
        fInertia = 500;
        fInertiaInverse = 1/fInertia;
        vPosition.x = 0;
        vPosition.y = 0;
        fWidth = 10;
        fLength = 20;
        fHeight = 5;
        fOrientation = 0;
        CD.x = -0.25f*fLength;
        CD.y = 0.0f;
        CD.z = 0.0f;
        CT.x = -0.5f*fLength;
        CT.y = 0.0f;
        CT.z = 0.0f;
        CPT.x = 0.5f*fLength;
        CPT.y = -0.5f*fWidth;
        CPT.z = 0.0f;
        CST.x = 0.5f*fLength;
        CST.y = 0.5f*fWidth;
        CST.z = 0.0f;
        ProjectedArea = (fLength + fWidth)/2 * fHeight; // an approximation
        ThrustForce = 500f;
    }

    public void calcLoads(){
        Vector3 Fb = new Vector3(); // stores the sum of forces
        Vector3 Mb = new Vector3(); // stores the sum of moments
        Vector3 Thrust = new Vector3(); // thrust vector
// reset forces and moments:
        vForces.x = 0.0f;
        vForces.y = 0.0f;
        vForces.z = 0.0f; // always zero in 2D
        vMoment.x = 0.0f; // always zero in 2D
        vMoment.y = 0.0f; // always zero in 2D
        vMoment.z = 0.0f;
        Fb.x = 0.0f;
        Fb.y = 0.0f;
        Fb.z = 0.0f;
        Mb.x = 0.0f;
        Mb.y = 0.0f;
        Mb.z = 0.0f;
// Define the thrust vector, which acts through the craft's CG
        Thrust.x = 1.0f;
        Thrust.y = 0.0f;
        Thrust.z = 0.0f; // zero in 2D
        Thrust.mul(ThrustForce);
// Calculate forces and moments in body space:
        Vector3 vLocalVelocity;
        float fLocalSpeed;
        Vector3 vDragVector;
        float tmp;
        Vector3 vResultant;
        Vector3 vtmp;

        // Calculate the aerodynamic drag force:
// Calculate local velocity:
// The local velocity includes the velocity due to
// linear motion of the craft,
// plus the velocity at each element
// due to the rotation of the craft.
        vtmp = vAngularVelocity.cpy().crossProduct(CD); // rotational part
        vLocalVelocity = vVelocityBody.add(vtmp);
// Calculate local air speed
        fLocalSpeed = vLocalVelocity.len();
// Find the direction in which drag will act.
// Drag always acts in line with the relative
// velocity but in the opposing direction
//        not true for wind calcs, drag acts in the direction of relative stream
        if(fLocalSpeed > tol)
        {
            vLocalVelocity.nor();
            vDragVector = vLocalVelocity.mul(-1);
// Determine the resultant force on the element.
            tmp = 0.5f * rho * fLocalSpeed*fLocalSpeed
                    * ProjectedArea;
            vResultant = vDragVector.cpy().mul( 0.5f * tmp);
// Keep a running total of these resultant forces
            Fb.add(vResultant);
// Calculate the moment about the CG
// and keep a running total of these moments
            vtmp = CD.cpy().crossProduct(vResultant);
            Mb.add(vtmp);
        }
        // Calculate the Port & Starboard bow thruster forces:
// Keep a running total of these resultant forces
        Fb.add(PThrust);
// Calculate the moment about the CG of this element's force
// and keep a running total of these moments (total moment)
        vtmp = CPT.cpy().crossProduct(PThrust);
        Mb.add(vtmp);
// Keep a running total of these resultant forces (total force)
        Fb.add(SThrust);
// Calculate the moment about the CG of this element's force
// and keep a running total of these moments (total moment)
        vtmp = CST.cpy().crossProduct(SThrust);
        Mb.add(vtmp);
// Now add the propulsion thrust
        Fb.add(Thrust); // no moment since line of action is through CG
// Convert forces from model space to earth space
//        should rotate about the z axis - not sure if works!!!!!
        vForces = Fb.cpy().rotate(fOrientation,0,0,1);
        vMoment.add(Mb);

    }

}



package com.badlogic.androidgames.routines;

import java.util.List;

import framework.classes.Vector2;

/**
 * Created by New PC 2012 on 17/01/2015.
 */
public class Routines {

    public static Routine sequence(Routine... routines) {
        Sequence sequence = new Sequence();
        for (Routine routine : routines) {
            sequence.addRoutine(routine);
        }
        return sequence;
    }

    public static Routine sequence(List<Routine> routines){
        Sequence sequence = new Sequence();
        for (Routine routine : routines) {
            sequence.addRoutine(routine);
        }
        return sequence;
    }

    public static Routine selector(Routine... routines) {
        Selector selector = new Selector();
        for (Routine routine : routines) {
            selector.addRoutine(routine);
        }
        return selector;
    }

    public static Routine reverseCourse(){
        return new ReverseCourse();
    }

    public static Routine wait(float seconds){
        return new Wait(seconds);
    }

    public static Routine decideBearing(float destx, float desty){
        return new DecideBearing(destx, desty);
    }

    public static Routine setMatchingBeating(){
        return new SetMatchingBearing();
    }

    public static Routine turnBroadside(){
        return new TurnBroadside();
    }

    public  static Routine deckLoadedTester(){return new DeckLoadedTester();}

    public static Routine decideBearing(){
        return new DecideBearing();
    }

    public static Routine chasingConditions(){
        return new ChasingConditions();
    }

    public static Routine closeToTest(){
        return new CloseToTest();
    }

    public static Routine closeToTest(float distance, boolean opposite){
        return new CloseToTest(distance, opposite);
    }

    public static Routine closeToTest(float distance){
        return new CloseToTest(distance);
    }

    public static Routine chooseGunDeck(){return new ChooseGunDeck();}

    public static Routine engageEnemy(){return  new EngageEnemy();}

    public static Routine closeOnTarget(){
        return new CloseOnTarget();
    }

    public static Routine fireGuns() {return new FireGuns(0);}

    public static Routine patrol(List<Vector2> points){
        return new Patrol(points);
    }

    public static Routine bearAway() {
        return new BearAway();
    }

    public static Routine turnToMatchBearing(){
        return new TurnToMatchBearing();
    }

    public static Routine repeat(Routine r, int times){
        return new Repeat(r,times);
    }
    public static Routine repeat(Routine r){
        return new Repeat(r);
    }
    public static Routine closeToTest(float destx, float desty){
        return new CloseToTest(destx, desty);
    }

    public static Routine moveTo(float posx, float posy){
        return new MoveTo(posx, posy);
    }


    public static SetCourse setCourse(float course){
        return new SetCourse(course);
    }public static SetCourse setCourse
            (){
        return new SetCourse();
    }
}

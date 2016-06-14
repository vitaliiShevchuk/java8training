package com.serena.defaultM;

public class ThreeResolution {

    public static void main(String[] args) {
        new ClassD().print();

        new ClassCA().print();

        new ClassBA().print();
    }

}

interface AA<A,B,C,D> {
    D doJob(A a, B b, C c, D d);
}

interface BB<A,B,C,D> {
    D doJob(A a, B b, C c, D d);
}

interface A {
    default void print() {
        System.out.println("<<A>>");
    }
}
interface B {
    default void print() {
        System.out.println("<<B>>");
    }
}
interface C extends A {
    default void print() {
        System.out.println("<<C>>");
    }
}

class D implements A {
    @Override
    public void print() {
        System.out.println("<<D>>");
    }
}

class ClassD extends D implements C {

}

class ClassCA implements C, A {

}

class ClassBA implements A, B {
    @Override
    public void print() {
        A.super.print();
        B.super.print();
        System.out.println("<<ClassBA>>");
    }
}
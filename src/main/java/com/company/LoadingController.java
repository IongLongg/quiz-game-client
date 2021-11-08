package com.company;

import javafx.event.ActionEvent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class LoadingController {
    DataInputStream dis;
    DataOutputStream dos;

    public void setDataStream(DataInputStream dis, DataOutputStream dos) throws IOException {
        this.dis = dis;
        this.dos = dos;
    }
}

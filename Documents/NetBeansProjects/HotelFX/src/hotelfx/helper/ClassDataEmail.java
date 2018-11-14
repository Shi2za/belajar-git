/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.helper;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Andreas
 */
public class ClassDataEmail {
    
    private final StringProperty sender;
    private final StringProperty received;
    private final StringProperty subject;
    private final StringProperty detail;
    private final StringProperty filePath;
    private final StringProperty fileName;

    public ClassDataEmail() {
    this.sender = new SimpleStringProperty();
    this.received = new SimpleStringProperty();
    this.subject = new SimpleStringProperty();
    this.detail = new SimpleStringProperty();
    this.filePath = new SimpleStringProperty();
    this.fileName = new SimpleStringProperty();

    }

    public ClassDataEmail(String sender, String received, String subject, String detail, String filePath, String fileName) {
    this();
    senderProperty().set(sender);
    receivedProperty().set(received);
    subjectProperty().set(subject);
    detailProperty().set(detail);
    filePathProperty().set(filePath);
    fileNameProperty().set(fileName);
    }

    public final StringProperty senderProperty() {
            return this.sender;
    }

    public String getSender() {
            return senderProperty().get();
    }

    public void setSender(String sender) {
            senderProperty().set(sender);
    }

    public final StringProperty receivedProperty() {
            return this.received;
    }

    public String getReceived() {
            return receivedProperty().get();
    }

    public void setReceived(String received) {
            receivedProperty().set(received);
    }

    public final StringProperty subjectProperty() {
            return this.subject;
    }

    public String getSubject() {
            return subjectProperty().get();
    }

    public void setSubject(String subject) {
            subjectProperty().set(subject);
    }

    public final StringProperty detailProperty() {
            return this.detail;
    }

    public String getDetail() {
            return detailProperty().get();
    }

    public void setDetail(String detail) {
            detailProperty().set(detail);
    }

    public final StringProperty filePathProperty() {
            return this.filePath;
    }

    public String getFilePath() {
            return filePathProperty().get();
    }

    public void setFilePath(String filePath) {
            filePathProperty().set(filePath);
    }

    public final StringProperty fileNameProperty() {
            return this.fileName;
    }

    public String getFileName() {
            return fileNameProperty().get();
    }

    public void setFileName(String fileName) {
            fileNameProperty().set(fileName);
    }



}

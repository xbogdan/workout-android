package com.boamfa.workout.classes;

/**
 * Created by bogdan on 30/12/15.
 */
public class History {
    public long id;
    public String tableName;
    public byte[] content;
    public long local_id;
    public String operation;
    public String timestamp;

    public History(long id, String tableName, byte[] content, long local_id,  String operation, String timestamp) {
        this.id = id;
        this.tableName = tableName;
        this.content = content;
        this.local_id = local_id;
        this.operation = operation;
        this.timestamp = timestamp;
    }
}

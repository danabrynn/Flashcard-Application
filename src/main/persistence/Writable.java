package persistence;

import org.json.JSONObject;

// Interface representing an object that can be returned as a JSON object
// Code taken from Writable interface in the JsonSerializationDemo
// (https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo)
public interface Writable {

    // Effects: returns this as JSON object
    JSONObject toJson();
}

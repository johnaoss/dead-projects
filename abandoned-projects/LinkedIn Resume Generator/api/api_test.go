package api

import (
	"io/ioutil"
	"reflect"
	"testing"
)

func TestParseJSON(t *testing.T) {
	invalidJSON, err := ParseJSON("{{}")
	if err == nil {
		t.Errorf("Invalid JSON should have returned an error, instead given %v\n", *invalidJSON)
	}
	file, err := ioutil.ReadFile("exampleresponse.json")
	if err != nil {
		t.Errorf("Couldn't read file exampleresponse.json, instead given: %s\n", err.Error())
	}
	validJSON, err := ParseJSON(string(file))
	if err != nil {
		t.Errorf("Couldn't parse valid JSON, instead given: %s\n", err.Error())
	}
	if reflect.TypeOf(validJSON) != reflect.TypeOf(&LinkedinProfile{}) {
		t.Errorf("Returned incorrect format, wanted: *LinkedinProfile, instead given: %+v\n", reflect.TypeOf(validJSON))
	}

}

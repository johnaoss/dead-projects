package resume

import "testing"

// Checks against valid, invalid, and erroneous data
func TestIsValidJSONResume(t *testing.T) {
	validData := [2]string{"{}", `{"meta": {}}`}
	invalidData := [2]string{`{"basics": {"email": "This is not an email address"}}`,
		`{"basics": [],"profiles": {}}`}
	panicData := [2]string{"not a json object", "{{}"}

	for _, elem := range validData {
		attempt, err := Validate(elem)
		if attempt != true {
			t.Errorf("Test failed, expected: true, got:  '%s'", err)
		}
	}

	for _, elem := range invalidData {
		attempt, err := Validate(elem)
		if attempt != false {
			t.Errorf("Test failed, expected: false, got:  '%s'", err)
		}
	}

	for _, elem := range panicData {
		var attempt bool
		var err []string
		defer func() {
			if r := recover(); r == nil {
				t.Errorf("The code did not panic. Instead returned %t, %s", attempt, err)
			}
		}()
		attempt, err = Validate(elem)
	}
}

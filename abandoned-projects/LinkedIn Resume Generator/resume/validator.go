package resume

import (
	"fmt"
	"io/ioutil"

	validator "github.com/xeipuuv/gojsonschema"
)

// Validate validates a JSON object according to the given schema.
// returns true if valid, false if not and an array of errors if it is invalid.
func Validate(json string) (bool, []string) {
	var errs []string
	jsonLoader := validator.NewStringLoader(json)
	schema, err := ioutil.ReadFile("./schema.json")
	if err != nil {
		panic(fmt.Errorf("could not find schema.json, ensure it is located within this directory"))
	}

	schemaLoader := validator.NewStringLoader(string(schema))
	result, err := validator.Validate(schemaLoader, jsonLoader)
	if err != nil {
		panic(fmt.Errorf("could not validate json"))
	}

	// Clean up the errors so we don't have to deal with package-specific types later.
	for _, elem := range result.Errors() {
		errs = append(errs, elem.Description())
	}
	return result.Valid(), errs
}

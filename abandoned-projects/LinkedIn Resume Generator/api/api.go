package api

import (
	"bytes"
	"encoding/json"
	"log"
)

// LinkedinProfile is used within this package as it is less useful than native types.
type LinkedinProfile struct {
	ProfileID           string    `json:"id"` // Unique ID
	FirstName           string    `json:"first_name"`
	LastName            string    `json:"last-name"`
	MaidenName          string    `json:"maiden-name"`
	FormattedName       string    `json:"formatted-name"`
	PhoneticFirstName   string    `json:"phonetic-first-name"`
	PhoneticLastName    string    `json:"phonetic-last-name"`
	Headline            string    `json:"headline"`      // User-created short description of themselves.
	Location            Location  `json:"location"`      // Shown as [USER_SPECIFIED_LOCATION, Country Code]
	Industry            string    `json:"industry"`      // TODO: Map this to a string.
	CurrentShare        string    `json:"current-share"` // User's latest shared/posted post.
	NumConnections      int       `json:"num-connections"`
	IsConnectionsCapped bool      `json:"num-connections-capped"` // Returns true if shown the cap of 500, if false, it's the user's actual connections.
	Summary             string    `json:"summary"`
	Specialties         string    `json:"specialties"` // Description of a user's specialties.
	Positions           Positions `json:"positions"`
	PictureURL          string    `json:"picture-url"`
	EmailAddress        string    `json:"email-address"` // Make sure EmailAddress is true for this to work, see the comment there for more details.
}

// Positions represents the result given by json:"positions"
type Positions struct {
	total  int
	values []Position
}

// Location specifies the users location
type Location struct {
	UserLocation string
	CountryCode  string
}

// Position represents a job held by the authorized user.
type Position struct {
	ID        string
	Title     string
	Summary   string
	StartDate string
	EndDate   string
	IsCurrent bool
	Company   Company
}

// Company represents the company of a job held by the authorized user.
type Company struct {
	ID       string
	Name     string
	Type     string
	Industry string
	Ticker   string
}

// ParseJSON converts a JSON string to a pointer to a LinkedinProfile.
func ParseJSON(s string) (*LinkedinProfile, error) {
	linkedinProfile := &LinkedinProfile{}
	bytes := bytes.NewBuffer([]byte(s))
	err := json.NewDecoder(bytes).Decode(linkedinProfile)
	if err != nil {
		log.Printf(err.Error())
		return nil, err
	}
	return linkedinProfile, nil
}

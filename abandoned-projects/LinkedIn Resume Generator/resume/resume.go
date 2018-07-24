package resume

import (
	"net/url"
	"os"
)

// JSONResume represents the data of the LinkedinProfile in a better struct.
type JSONResume struct {
	Basics       Basics
	Work         []Work
	Volunteer    []Volunteer
	Education    []Education
	Awards       []Award
	Publications []Publication
	Skills       []Skill
	Languages    []Language
	Interests    []Interest
	References   []Reference
}

// Basics covers all the essential parts of a profile, (i.e. Name, Website, Summary)
type Basics struct {
	Name     string
	Label    string
	Picture  os.File
	Email    string
	Phone    string
	Website  url.URL
	Summary  string
	Location Location
	Profiles []SiteProfile
}

// Location covers where the user lives.
type Location struct {
	Address     string
	PostalCode  string
	City        string
	CountryCode string
	Region      string //Province/State
}

// SiteProfile represents the profile for various social networks (i.e. Twitter)
type SiteProfile struct {
	Network  string
	Username string
	URL      string
}

// Work describes a job position.
type Work struct {
	Company    string
	Position   string
	Website    string
	StartDate  string
	EndDate    string
	Summary    string
	Highlights []string
}

// Volunteer company is the same as organization.
type Volunteer Work

// Education describes a completed education
type Education struct {
	Institution string
	Position    string
	Website     string
	StartDate   string
	EndDate     string
	GPA         int
	Highlights  []string
}

// Award describes an award.
type Award struct {
	Title   string
	Date    string
	Awarder string
	Summary string
}

// Publication describes a published piece of work.
type Publication struct {
	Name        string
	Publisher   string
	ReleaseDate string
	Website     string
	Summary     string
}

// Skill describes a skill
type Skill struct {
	Name     string
	Level    string
	Keywords []string
}

// Language describes a language, with a proficiency level (i.e. Native Speaker, Fluent...)
type Language struct {
	Name  string
	Level string
}

// Interest describes a particular interest someone has, along with certain keywords
// (i.e. Compiler Development may have keywords such as `gcc` or `clang`)
type Interest struct {
	Name     string
	Keywords []string
}

// Reference is barely defined by the JSONResume schema, and as such will barely be described.
// Info should contain material like e-mail addresses and phone numbers, but not both.
// Potential map[string]string{} with standardized keywords like `email` and `phone`
type Reference struct {
	Name string
	Info string
}

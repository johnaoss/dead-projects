package router

import (
	"crypto/rand"
	"io/ioutil"
	"net/http"

	"github.com/gorilla/sessions"
	"golang.org/x/oauth2"
	"golang.org/x/oauth2/linkedin"
)

// TODO MAKE SECRET.
var store = sessions.NewCookieStore([]byte("something-very-secret"))

const (
	// CAUTION: This requests EVERYTHING and as such requires advanced LinkedIn API priviledges.
	requestURL = "https://api.linkedin.com/v1/people/~:(id,first-name,email-address,last-name,headline,picture-url,industry,summary,specialties,positions:(id,title,summary,start-date,end-date,is-current,company:(id,name,type,size,industry,ticker)),educations:(id,school-name,field-of-study,start-date,end-date,degree,activities,notes),associations,interests,num-recommenders,date-of-birth,publications:(id,title,publisher:(name),authors:(id,name),date,url,summary),patents:(id,title,summary,number,status:(id,name),office:(name),inventors:(id,name),date,url),languages:(id,language:(name),proficiency:(level,name)),skills:(id,skill:(name)),certifications:(id,name,authority:(name),number,start-date,end-date),courses:(id,name,number),recommendations-received:(id,recommendation-type,recommendation-text,recommender),honors-awards,three-current-positions,three-past-positions,volunteer)?format=json"
)

// Credentials contains the stuff we need to authenticate OAuth2.0 calls.
type Credentials struct {
	ClientID     string
	ClientSecret string
}

// initAuth initializes the authentication scheme, used in router.go
func initAuth(creds *Credentials) *oauth2.Config {
	config := &oauth2.Config{ClientID: creds.ClientID,
		ClientSecret: creds.ClientSecret,
		Endpoint:     linkedin.Endpoint,
		RedirectURL:  "https://yasp.johnoss.com/auth",
		Scopes:       []string{"r_emailaddress", "r_basicprofile"},
	}
	return config
}

// generateState generates a random set of characters to be used to ensure state is preserved.
func generateState() string {
	b := make([]byte, 32)
	rand.Read(b)
	return string(b)
}

// getLoginURL returns a URL with the state and other parameters specified by the oauth config.
func getLoginURL(s string) string {
	return authConf.AuthCodeURL(s)
}

// authHandler gets and stores the data we need from the user after a successful login.
func authHandler(c *AppContext, w http.ResponseWriter, r *http.Request) (int, error) {
	session, err := store.Get(r, "yasp-resume")
	if err != nil {
		w.Write([]byte("Couldn't retrieve cookies"))
		return http.StatusInternalServerError, nil
	}
	retrievedState := session.Values["state"]
	if getSessionValue(retrievedState) != r.Header.Get("state") {
		w.Write([]byte("Couldn't verify state"))
		return http.StatusUnauthorized, nil
	}
	params := r.URL.Query()
	tok, err := authConf.Exchange(oauth2.NoContext, params.Get("code"))
	if err != nil {
		w.Write([]byte("Couldn't exchange tokens"))
		return http.StatusBadRequest, nil
	}
	client := authConf.Client(oauth2.NoContext, tok)
	resp, err := client.Get(requestURL)
	if err != nil {
		w.Write([]byte("Couldn't get data from API"))
		return http.StatusBadRequest, nil
	}
	data, _ := ioutil.ReadAll(resp.Body)
	defer resp.Body.Close()
	session.Values["user-info"] = data
	session.Values["authenticated"] = true
	defer session.Save(r, w)
	//http.Redirect(w, r, "/", 303)
	w.Write([]byte(data))
	return http.StatusOK, nil
}

func clearState(w http.ResponseWriter, r *http.Request) {
	session, _ := store.Get(r, "yasp-resume")
	session.Values["authenticated"] = false
	session.Values["user-info"] = ""
	defer session.Save(r, w)
}

// getSessionValue extract value from a session as a string
// e.g. getSessionValue(session.Values["authenticated"]) will return "true"
func getSessionValue(f interface{}) string {
	if f != nil {
		if foo, ok := f.(string); ok {
			return foo
		}
	}
	return ""
}

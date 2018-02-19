(ns expenses.oauth
  (:require [expenses.config :refer [env]]
            [oauth.client :as oauth]
            [mount.core :refer [defstate]]
            [clojure.tools.logging :as log]))

(defstate twitter-consumer
  :start (oauth/make-consumer
           (env :oauth-twitter-consumer-key)
           (env :oauth-twitter-consumer-secret)
           (env :request-twitter-token-uri)
           (env :access-twitter-token-uri)
           (env :authorize-twitter-uri)
           :hmac-sha1))

(defstate google-consumer
          :start (oauth/make-consumer
                   (env :oauth-google-consumer-key)
                   (env :oauth-google-consumer-secret)
                   (env :request-google-token-uri)
                   (env :access-google-token-uri)
                   (env :authorize-google-uri)
                   :hmac-sha1))

(defn oauth-callback-uri
  "Generates the oauth request callback URI"
  [{:keys [headers]}]
  (str (headers "x-forwarded-proto") "://" (headers "host") "/oauth/twitter-callback"))

(defn fetch-request-token
  "Fetches a request token."
  [request]
  (let [callback-uri (oauth-callback-uri request)]
    (log/info "Fetching request token using callback-uri" callback-uri)
    (oauth/request-token twitter-consumer (oauth-callback-uri request))))

(defn fetch-access-token
  [request_token]
  (oauth/access-token twitter-consumer request_token (:oauth_verifier request_token)))

(defn auth-redirect-uri
  "Gets the URI the user should be redirected to when authenticating."
  [request-token]
  (str (oauth/user-approval-uri twitter-consumer request-token)))

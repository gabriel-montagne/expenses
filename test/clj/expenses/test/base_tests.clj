(ns expenses.test.base-tests
  (:require
    [ak-dbg.core :refer :all]
    [clojure.test :refer :all]
    [clojure.data.json :as json]
    [ring.mock.request :refer :all]))



(defn test-post-request [uri payload]
  (content-type
    (body
      (request :post uri)
      (json/write-str payload))
    "application/json"))

(defn test-patch-request [uri payload]
  (content-type
    (body
      (request :patch uri)
      (json/write-str payload))
    "application/json"))

(defn test-put-request [uri payload]
  (content-type
    (body
      (request :put uri)
      (json/write-str payload))
    "application/json"))

#!/usr/bin/env bash
#
# Sample usage:
#
#   HOST=localhost PORT=8080 ./test.sh
#
: ${HOST=localhost}
: ${PORT=7006}
: ${CONTR_ID_REVS_RECS=1}
: ${CONTR_ID_NOT_FOUND=13}

function assertCurl() {

  local expectedHttpCode=$1
  local curlCmd="$2 -w \"%{http_code}\""
  local result=$(eval $curlCmd)
  local httpCode="${result:(-3)}"
  RESPONSE='' && (( ${#result} > 3 )) && RESPONSE="${result%???}"

  if [ "$httpCode" = "$expectedHttpCode" ]
  then
    if [ "$httpCode" = "200" ]
    then
      echo "Test OK (HTTP Code: $httpCode)"
    else
      echo "Test OK (HTTP Code: $httpCode, $RESPONSE)"
    fi
  else
    echo  "Test FAILED, EXPECTED HTTP Code: $expectedHttpCode, GOT: $httpCode, WILL ABORT!"
    echo  "- Failing command: $curlCmd"
    echo  "- Response Body: $RESPONSE"
    exit 1
  fi
}

function assertEqual() {

  local expected=$1
  local actual=$2

  if [ "$actual" = "$expected" ]
  then
    echo "Test OK (actual value: $actual)"
  else
    echo "Test FAILED, EXPECTED VALUE: $expected, ACTUAL VALUE: $actual, WILL ABORT"
    exit 1
  fi
}

set -e

echo "HOST=${HOST}"
echo "PORT=${PORT}"


# Verify that a normal request works
assertCurl 200 "curl http://$HOST:$PORT/contract/$CONTR_ID_REVS_RECS -s"
assertEqual $CONTR_ID_REVS_RECS $(echo $RESPONSE | jq .contractId)

# Verify that a 404 (Not Found) error is returned for a non-existing contractId ($CONTR_ID_NOT_FOUND)
assertCurl 404 "curl http://$HOST:$PORT/contract/$CONTR_ID_NOT_FOUND -s"
assertEqual "No contract found for contractId: $CONTR_ID_NOT_FOUND" "$(echo $RESPONSE | jq -r .message)"

# Verify that a 422 (Unprocessable Entity) error is returned for a contractId that is out of range (-1)
assertCurl 422 "curl http://$HOST:$PORT/contract/-1 -s"
assertEqual "\"Invalid contractId: -1\"" "$(echo $RESPONSE | jq .message)"

# Verify that a 400 (Bad Request) error error is returned for a contractId that is not a number, i.e. invalid format
assertCurl 400 "curl http://$HOST:$PORT/contract/invalidContractId -s"
assertEqual "\"Type mismatch.\"" "$(echo $RESPONSE | jq .message)"

echo "End, all tests OK:" `date`

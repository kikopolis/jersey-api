#!/bin/sh
cd "$(dirname "$0")" || exit
# Create certificate authority certificate and key
openssl genrsa -out ca.priv 4096
openssl req -x509 -new -nodes -sha256 -days 365 -key ca.priv -out ca.crt -subj "/C=EE/O=kikopolis/CN=ca"
# Create server certificate and key
openssl genrsa -out jerseyapi.priv 4096
openssl req -new -key jerseyapi.priv -out jerseyapi.csr -subj "/C=EE/O=kikopolis/CN=jerseyapi"
# Add domains into extension file that will use this cert
cat <<EOF >jerseyapi.ext
subjectAltName = @alt_names
[alt_names]
DNS.1 = localhost
DNS.2 = kikopolisapi.com
DNS.3 = www.kikopolisapi.com
EOF
# Create api signing
openssl x509 -req -in jerseyapi.csr -CA ca.crt -CAkey ca.priv -out jerseyapi.crt -CAcreateserial -days 365 -sha256 -extfile jerseyapi.ext
# Put certs in a keystore for use in server
openssl pkcs12 -export -in jerseyapi.crt -inkey jerseyapi.priv -certfile jerseyapi.crt -password pass:changeit -name jerseyapi -out jerseyapi.p12
# Generate PUB and PEM
openssl pkcs12 -in jerseyapi.p12 -out jerseyapi.pub -clcerts -nokeys -passin pass:changeit
openssl pkcs12 -in jerseyapi.p12 -out jerseyapi.pem -nodes -passin pass:changeit

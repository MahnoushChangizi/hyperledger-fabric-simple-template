#!/bin/bash
export PATH=${PWD}/../bin:$PATH 
export FABRIC_CFG_PATH=$PWD/../config/ 
export CORE_PEER_TLS_ENABLED=true 
export CORE_PEER_LOCALMSPID="Org2MSP" 
export CORE_PEER_TLS_ROOTCERT_FILE=${PWD}/organizations/peerOrganizations/org2.example.com/peers/peer0.org2.example.com/tls/ca.crt 
export CORE_PEER_MSPCONFIGPATH=${PWD}/organizations/peerOrganizations/org2.example.com/users/User1@org2.example.com/msp 
export CORE_PEER_ADDRESS=localhost:9051

# for submitting transactions
peer chaincode invoke -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --tls --cafile \
"${PWD}/organizations/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem" \
--peerAddresses localhost:7051 --tlsRootCertFiles ${PWD}/organizations/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.crt \
--peerAddresses localhost:9051 --tlsRootCertFiles ${PWD}/organizations/peerOrganizations/org2.example.com/peers/peer0.org2.example.com/tls/ca.crt \
-C mychannel -n basic -c '{"function":"myTransaction2","Args":["asset99"]}'

# for evaluation transactions
peer chaincode query -C mychannel -n private -c '{"function":"myTransactionK","Args":["asset99"]}'

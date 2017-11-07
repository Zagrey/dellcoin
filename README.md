# dellcoin

geth --dev --minerthreads 1 --rpccorsdomain "*" --rpc --rpcaddr "0.0.0.0" --rpcapi "admin,debug,miner,shh,txpool,personal,eth,net,web3" console

./mist --rpc http://localhost:8545  --swarmurl "null"

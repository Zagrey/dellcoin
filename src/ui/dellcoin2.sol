pragma solidity ^0.4.0;


contract DellCoin {

    mapping (bytes32 => int256) public serverSeedMap;
    mapping (bytes32 => int256) public clientSeedMap;
    mapping (bytes32 => int256) public clientOrigSumMap;
    mapping (bytes32 => uint256) public clientCheckCountMap;
    mapping (bytes32 => uint256) public clientFileSizeMap;

    mapping (bytes32 => address) public clients; // file -> client
    mapping (bytes32 => address) public servers; // file -> server

    address public owner;
    bytes32 private name;

    function DellCoin() public {
        owner = msg.sender;
    }

    function setName(bytes32 n) public {
        name = n;
    }

    function getName() public constant returns (bytes32) {
        return name;
    }

    modifier onlyOwner {
        require(msg.sender == owner);
        _;
    }

    function() public payable {
//        buy();
    }

    // клиент выложил файл в контракт
    function addClientFile(bytes32 file, int256 clientOrigSum, int256 clientSeed, uint256 clientCheckCount, uint256 fileSize) public {
        clientSeedMap[file] = clientSeed;
        clientOrigSumMap[file] = clientOrigSum;
        clientCheckCountMap[file] = clientCheckCount;
        clientFileSizeMap[file] = fileSize;

        clients[file] = msg.sender;
    }

    // сервер для файла клиента выложил ключ
    function addServerSeed(bytes32 file, int256 serverSeed) public {
        serverSeedMap[file] = serverSeed;

        servers[file] = msg.sender;
    }

    // вызывает клиент, если сумма сошлась - зачислить серверу деньги, клиенту отдать ключ
    function checkValid(bytes32 file, int256 serverSum) public view returns (int256) {
//        int256 clientSum = clientOrigSumMap[file];
        require(serverSum > 0);

//        if (clientOrigSum == serverSum - randSum) {
//            return true;
//        }

//        int256 randSum = 5;

//        require(clientSum == serverSum - randSum);

        return serverSeedMap[file];
    }

    function kill() public onlyOwner {
        selfdestruct(owner);
    }
}


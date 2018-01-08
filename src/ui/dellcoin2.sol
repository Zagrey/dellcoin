pragma solidity ^0.4.0;


contract DellCoin {

    mapping (bytes32 => uint256) public serverSeedMap;
    mapping (bytes32 => uint256) public clientSeedMap;
    mapping (bytes32 => uint256) public clientOrigSumMap;
    mapping (bytes32 => uint256) public clientCheckCountMap;
    mapping (bytes32 => uint256) public clientFileSizeMap;

    mapping (bytes32 => address) public clients;
    mapping (bytes32 => address) public servers;

    address private owner;

    function DellCoin() public {
        owner = msg.sender;
    }

    modifier onlyOwner {
        require(msg.sender == owner);
        _;
    }

    function() public payable {
//        buy();
    }

    // клиент выложил файл в контракт
    function addClientFile(bytes32 file, uint clientOrigSum, uint clientSeed, uint clientCheckCount, uint fileSize) public {
        clientSeedMap[file] = clientSeed;
        clientOrigSumMap[file] = clientOrigSum;
        clientCheckCountMap[file] = clientCheckCount;
        clientFileSizeMap[file] = fileSize;

        clients[file] = msg.sender;
    }

    // сервер для файла клиента выложил ключ
    function addServerSeed(bytes32 file, uint serverSeed) public {
        serverSeedMap[file] = serverSeed;

        servers[file] = msg.sender;
    }

    // вызывает клиент, если сумма сошлась - зачислить серверу деньги, клиенту отдать ключ
    function checkValid(bytes32 file, uint serverSum) public view returns (uint256) {
        uint256 clientSum = clientOrigSumMap[file];

//        if (clientOrigSum == serverSum - randSum) {
//            return true;
//        }

        uint randSum = 5;

        require(clientSum == serverSum - randSum);

        return serverSeedMap[file];
    }

    function kill() public onlyOwner {
        selfdestruct(owner);
    }
}


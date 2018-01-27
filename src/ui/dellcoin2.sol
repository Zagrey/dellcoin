pragma solidity ^0.4.0;


contract DellCoin {
    struct Random {
        int32 seed;
    }

    mapping(bytes32 => int32) public serverKeyMap;
    // file -- seed
    mapping(bytes32 => int32) public clientSeedMap;
    mapping(bytes32 => int256) public clientOrigSumMap;
    mapping(bytes32 => int256) public clientCheckCountMap;
    mapping(bytes32 => int256) public clientFileSizeMap;

    mapping(bytes32 => address) public File2Client; // file -> client
    mapping(bytes32 => address) public File2Server; // file -> server

    mapping (address => uint) public balances;

    event test_value(uint256 indexed arg);

    address public owner;
    bytes32 private name;

    event KeyDisclosure(bytes32 file, int256 key);
    event NewBalance(address client, uint balance);

    function () public payable {
        balances[msg.sender] += msg.value;
        NewBalance(msg.sender, balances[msg.sender]);
    }

    function DellCoin() public {
        owner = msg.sender;
    }

    function setName(bytes32 n) public {
        name = n;
    }

    function getName() public constant returns (bytes32) {
        return name;
    }

    function getBalance() public constant returns (uint) {
        return balances[msg.sender];
    }

    modifier onlyOwner {
        require(msg.sender == owner);
        _;
    }

    // клиент выложил файл в контракт
    function addClientFile(bytes32 file, int256 clientOrigSum, int32 clientSeed, int256 clientCheckCount, int256 fileSize) public {
        clientSeedMap[file] = clientSeed;
        clientOrigSumMap[file] = clientOrigSum;
        clientCheckCountMap[file] = clientCheckCount;
        clientFileSizeMap[file] = fileSize;

        File2Client[file] = msg.sender;
    }

    // сервер для файла клиента выложил ключ
    function addServerKey(bytes32 file, int256 serverKey) public {
        serverKeyMap[file] = serverKey;
        File2Server[file] = msg.sender;
    }

    // вызывает клиент, если сумма сошлась - зачислить серверу деньги, клиенту отдать ключ
    function checkValid(bytes32 file, int256 serverSum) public returns (bool) {
        //        int256 clientSum = clientOrigSumMap[file];
        require(serverSum > 0);

var serverRandom = Random(generateSeed(serverSeedMap[file]));

int32[] memory serverRandomSequence = new int32[](uint256(clientFileSizeMap[file]));
        for (uint32 i = 0; i < clientFileSizeMap[file]; i++) {
            var sr = nextInt(serverRandom, 1000);
            serverRandomSequence[i] = sr;
        }

int64 randSum= 0;
        var clientRandom = Random(generateSeed(clientSeedMap[file]));
        for (i = 0; i < clientCheckCountMap[file]; i++) {
            var cr = nextInt(clientRandom, int32(clientFileSizeMap[file]));
            randSum += serverRandomSequence[uint256(cr)];
        }

        if (clientOrigSumMap[file] == (serverSum - randSum)) {
            if (serverKeyMap[file]!=0) {
                int256 key = serverKeyMap[file];
                uint amount = 10 ether;
                uint ownerFee = 1 ether;
                address client = File2Client[file];
                address server = File2Server[file];
                balances[client] -= amount + ownerFee;
                NewBalance(client, balances[client]);
                server.transfer(amount);
                owner.transfer(ownerFee);
                KeyDisclosure(file, key);
                return true;
            }
        }

        return false;
    }

    function kill() public onlyOwner {
        selfdestruct(owner);
    }

    function generateSeed(int32 seed) public pure returns (int32) {
        seed = seed % 2147483647;
        if (seed <= 0) {
            seed += 2147483646;
        }
        return seed;
    }

    function nextInt(Random random, int32 range) public pure returns (int32) {
        random.seed = (random.seed * 16807) % 2147483647;
        return (random.seed - 1) % 2147483646 % range;
    }
}

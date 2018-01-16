
angular.module("dellCoinClient", [])
.constant("baseUrl", "http://localhost:8080/")
.controller("defaultCtrl", function ($scope, $http, $log, baseUrl) {

    // текущее педставление
    $scope.clientFiles = [];
    $scope.clientFile2Peer = [];
    $scope.serverFiles = [];
    $scope.clientFilesSlices = [];
    $scope.fileContent = "";
    $scope.peers = [];
    $scope.web = null;

    $scope.addFile = function () {

        var file = {"content": $scope.fileContent, "id": null, "hash": ""};

        // $http.post(baseUrl + "file", file).success(function (item) {
        //     $scope.clientFiles.push(item);
        // });

        $log.info("Add file: " + file.content);

        $http({
            method: 'POST',
            url: baseUrl + "clientFile",
            data: file
        }).then(function successCallback(response) {
            $scope.clientFiles.push(response.data);
            $scope.fileContent = "";
            $log.info("File added: " + response.data.hash);

            var tx = $scope.dellCoinContractInstance.addClientFile(response.data.hash.substr(0, 32), response.data.clientOrigSum, response.data.clientSeed, response.data.clientCheckCount, response.data.size, {gas:200000});
            $log.info("Add into contract file: " + response.data.hash.substr(0, 32) + ", TX: " + tx);

            $log.info("Set name TX: " + $scope.dellCoinContractInstance.setName(response.data.hash.substr(0, 32)));

            $scope.getClientFilesSlices();
        }, function errorCallback(response) {
            $log.error(response);
        });
    };

    $scope.deleteFile = function (file) {

        $log.info("Delete file: " + file.hash);

        $http({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            dataType: 'json',
            method: 'DELETE',
            url: baseUrl + "clientFiles/" + file.id,
            data: file
        }).then(function successCallback() {
            $scope.clientFiles.splice($scope.clientFiles.indexOf(file), 1);
            $log.info("File deleted.");
        }, function errorCallback(response) {
            $log.error(response);
        });
    };

    $scope.verifyFile = function (file) {

        var clientFileHash = file.hash;
        var clientFileId = file.id;

        for (var i = 0; i < $scope.peers.length; i++) {

            $log.info("Verify client file " + file.hash + " on server: " + $scope.peers[i].address);

            var sliceH = [];
            for (var j = 0; j < $scope.clientFilesSlices.length; j++) {

                var peerUrl = "http://" + $scope.peers[i].address + ":8080/";
                item = $scope.clientFilesSlices[j];

                if (item.fileId !== clientFileId){
                    continue;
                }

                $log.info("Check hash for block [" + item.startIndex + " - " + item.endIndex +  "]...");

                sliceH[item.startIndex + " - " + item.endIndex] = item.hash;

                $http({
                    method: 'POST',
                    url: peerUrl + "serverFiles/verify/" + clientFileHash,
                    data: {"hash": clientFileHash, "start": item.startIndex, "end": item.endIndex}
                }).then(function successCallback(response) {

                    if (response.data.hash !== undefined && response.data.hash === sliceH[response.data.start  + " - " + response.data.end]){
                        $log.info("Slice hash OK");
                    } else {
                        $log.info("Slice hash WRONG: " + response.data.start  + " - " + response.data.end);
                        alert("Slice hash WRONG: " + response.data.hash)
                    }

                }, function errorCallback(response) {
                    $log.error(response);
                });


            }
        }
    };

    $scope.updateClientFile = function (file) {

        $log.info("Update client file: " + file.hash);

        $http({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            dataType: 'json',
            method: 'PUT',
            url: baseUrl + "clientFiles/" + file.id,
            data: file
        }).then(function successCallback() {
            // $scope.clientFiles.splice($scope.clientFiles.indexOf(file), 1);
            $log.info("File updated.");
        }, function errorCallback(response) {
            $log.error(response);
        });
    };

    $scope.addClientFile2Peer = function (file, peer) {

        var file2peer = {"file": file, "id": null, "peer": peer};

        $log.info("Add file: " + file + " to peer: " + peer);

        $http({
            method: 'POST',
            url: baseUrl + "clientFiles2Peer",
            data: file2peer
        }).then(function successCallback(response) {
            $scope.clientFile2Peer.push(response.data);

            $log.info("clientFiles2Peer added: " + response.data.id);
        }, function errorCallback(response) {
            $log.error(response);
        });
    };

    $scope.deleteServerFile = function (file) {

        $log.info("Delete server file: " + file.hash);

        $http({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            dataType: 'json',
            method: 'DELETE',
            url: baseUrl + "serverFiles/" + file.id,
            data: file
        }).then(function successCallback() {
            $scope.serverFiles.splice($scope.serverFiles.indexOf(file), 1);
            $log.info("File deleted.");
        }, function errorCallback(response) {
            $log.error(response);
        });
    };

    $scope.decodeFile = function (file) {
        $log.info("Decode file: " + file.hash);


        $http({
            method: 'GET',
            url: baseUrl + "decode/" + file.hash
        }).then(function successCallback(response) {

            $log.info("File decoded successfully: " + response.data.content);
            $scope.getClientFiles();
            $scope.getServerFiles();

        }, function errorCallback(response) {
            $log.error(response);
            alert("Decode failed: " + response.data.content)
        });
    };

    $scope.downloadFile = function (file) {
        $log.info("Download file: " + file.hash);

        var peerAddress = "";
        var peer = -1;
        for (var i = 0; i < $scope.clientFile2Peer.length; i++) {
            if ($scope.clientFile2Peer[i].file === file.id){
                peer = $scope.clientFile2Peer[i].peer;
                break;
            }
        }
        for (i = 0; i < $scope.peers.length; i++) {
            if ($scope.peers[i].id === peer){
                peerAddress = $scope.peers[i].address;
                break;
            }
        }

        if (peerAddress !== ""){
            var peerUrl = "http://" + peerAddress + ":8080/";

            $http({
                method: 'GET',
                url: peerUrl + "serverFiles/" + file.hash
            }).then(function successCallback(response) {

                $log.info("File downloaded: " + response.data.content);

                if (file.hash === response.data.hash){

                    file.content = response.data.content;
                    $scope.updateClientFile(file);

                    var tx = $scope.dellCoinContractInstance.addServerSeed(response.data.hash.substr(0, 32), response.data.serverSeed, {gas:200000});
                    $log.info("Add server seed into contract: " + response.data.hash.substr(0, 32) + ", seed: " + response.data.serverSeed +  ", tx: " + tx);

                } else {
                    $log.error("Uploaded file has different hash. Remove file on server.");
                }
            }, function errorCallback(response) {
                $log.error(response);
                alert("Download failed (file not found): " + file.hash)
            });
        }
    };

    $scope.uploadFile = function (file) {

        if (file.content === "------------- wiped -------------"){
            $log.warn("File already uploaded: " + file.hash);
            return;
        }

        for (var i = 0; i < $scope.peers.length; i++) {

            $log.info("Upload file '" + file.id + "' to peer: " + $scope.peers[i].address);

            var fileToUpload = file;
            // fileToUpload.id = 0;

            var peerUrl = "http://" + $scope.peers[i].address + ":8080/";
            var peerId = $scope.peers[i].id;

            $http({
                method: 'POST',
                url: peerUrl + "serverFile",
                data: fileToUpload
            }).then(function successCallback(response) {
                // $scope.clientFiles.push(response.data);
                // $scope.fileContent = "";
                $log.info("File uploaded: " + response.data);

                if (file.hash === response.data.hash){

                    $log.info("File uploaded successfully. Hashes are equals.");
                    $log.info("Wipe content for uploaded file.");

                    file.content = "------------- wiped -------------";
                    $scope.updateClientFile(file);
                    $scope.addClientFile2Peer(file.id, peerId);
                    $scope.getServerFiles();
                } else {
                    $log.error("Uploaded file has different hash. Remove file on server.");
                    $scope.deleteServerFile(response.data);
                }
            }, function errorCallback(response) {
                $log.error(response);
            });
        }
    };

    $scope.init = function () {
        $scope.getClientFiles();
        $scope.getServerFiles();
        $scope.getPeers();
        $scope.getClientFiles2Peers();
        $scope.getClientFilesSlices();
        $scope.web3Init();
    };

    $scope.web3Init = function () {
        $scope.web3 = new Web3(new Web3.providers.HttpProvider("http://localhost:8545"));

        if($scope.web3.isConnected) {
            console.log("Ethereum local network connected.");

            $scope.web3.eth.defaultAccount = $scope.web3.eth.accounts[0];
            $scope.dellCoinContract = $scope.web3.eth.contract([ { "constant": true, "inputs": [], "name": "getName", "outputs": [ { "name": "", "type": "bytes32", "value": "0x0000000000000000000000000000000000000000000000000000000000000000" } ], "payable": false, "stateMutability": "view", "type": "function" }, { "constant": false, "inputs": [], "name": "kill", "outputs": [], "payable": false, "stateMutability": "nonpayable", "type": "function" }, { "constant": false, "inputs": [ { "name": "n", "type": "bytes32" } ], "name": "setName", "outputs": [], "payable": false, "stateMutability": "nonpayable", "type": "function" }, { "constant": false, "inputs": [ { "name": "file", "type": "bytes32" }, { "name": "clientOrigSum", "type": "int256" }, { "name": "clientSeed", "type": "int256" }, { "name": "clientCheckCount", "type": "uint256" }, { "name": "fileSize", "type": "uint256" } ], "name": "addClientFile", "outputs": [], "payable": false, "stateMutability": "nonpayable", "type": "function" }, { "constant": true, "inputs": [ { "name": "", "type": "bytes32" } ], "name": "clientOrigSumMap", "outputs": [ { "name": "", "type": "int256", "value": "0" } ], "payable": false, "stateMutability": "view", "type": "function" }, { "constant": true, "inputs": [ { "name": "", "type": "bytes32" } ], "name": "servers", "outputs": [ { "name": "", "type": "address", "value": "0x0000000000000000000000000000000000000000" } ], "payable": false, "stateMutability": "view", "type": "function" }, { "constant": true, "inputs": [ { "name": "", "type": "bytes32" } ], "name": "clientCheckCountMap", "outputs": [ { "name": "", "type": "uint256", "value": "0" } ], "payable": false, "stateMutability": "view", "type": "function" }, { "constant": true, "inputs": [ { "name": "", "type": "bytes32" } ], "name": "clientSeedMap", "outputs": [ { "name": "", "type": "int256", "value": "0" } ], "payable": false, "stateMutability": "view", "type": "function" }, { "constant": false, "inputs": [ { "name": "file", "type": "bytes32" }, { "name": "serverSeed", "type": "int256" } ], "name": "addServerSeed", "outputs": [], "payable": false, "stateMutability": "nonpayable", "type": "function" }, { "constant": true, "inputs": [ { "name": "", "type": "bytes32" } ], "name": "serverSeedMap", "outputs": [ { "name": "", "type": "int256", "value": "0" } ], "payable": false, "stateMutability": "view", "type": "function" }, { "constant": true, "inputs": [ { "name": "", "type": "bytes32" } ], "name": "clients", "outputs": [ { "name": "", "type": "address", "value": "0x0000000000000000000000000000000000000000" } ], "payable": false, "stateMutability": "view", "type": "function" }, { "constant": true, "inputs": [ { "name": "file", "type": "bytes32" }, { "name": "serverSum", "type": "int256" } ], "name": "checkValid", "outputs": [ { "name": "", "type": "int256", "value": "0" } ], "payable": false, "stateMutability": "view", "type": "function" }, { "constant": true, "inputs": [ { "name": "", "type": "bytes32" } ], "name": "clientFileSizeMap", "outputs": [ { "name": "", "type": "uint256", "value": "0" } ], "payable": false, "stateMutability": "view", "type": "function" }, { "inputs": [], "payable": false, "stateMutability": "nonpayable", "type": "constructor" }, { "payable": true, "stateMutability": "payable", "type": "fallback" } ]);
            $scope.dellCoinContractInstance = $scope.dellCoinContract.at('0xe40EcfF06b34F89E101e201D4da6F83B55D5Ebac');
            $scope.web3.personal.unlockAccount($scope.web3.eth.defaultAccount,'1q2w3e4r',1000000);
        } else {
            console.log("Ethereum local network not connected.");
        }

    };

    $scope.getClientFiles2Peers = function () {
        $http({
            method: 'GET',
            url: baseUrl + "clientFiles2Peers"
        }).then(function successCallback(response) {
            $scope.clientFile2Peer = response.data;
        }, function errorCallback(response) {
            $log.error(response);
        });
    };

    $scope.getClientFilesSlices = function () {
        $http({
            method: 'GET',
            url: baseUrl + "slices"
        }).then(function successCallback(response) {
            $scope.clientFilesSlices = response.data;
        }, function errorCallback(response) {
            $log.error(response);
        });
    };

    $scope.getClientFiles = function () {
        $http({
            method: 'GET',
            url: baseUrl + "clientFiles"
        }).then(function successCallback(response) {
            $scope.clientFiles = response.data;
        }, function errorCallback(response) {
            $log.error(response);
        });
    };

    $scope.getServerFiles = function () {
        $http({
            method: 'GET',
            url: baseUrl + "serverFiles"
        }).then(function successCallback(response) {
            $scope.serverFiles = response.data;
        }, function errorCallback(response) {
            $log.error(response);
        });
    };

    $scope.getPeers = function () {
        $http({
            method: 'GET',
            url: baseUrl + "peers"
        }).then(function successCallback(response) {
            $scope.peers = response.data;
        }, function errorCallback(response) {
            $log.error(response);
        });
    };


    // получение всех данных из модели
    $scope.refresh = function () {
        // HTTP GET
        // получение всех данных через GET запрос по адрес хранящемуся в baseUrl
        $http.get(baseUrl).success(function (data) {
            $scope.items = data;
        });
    };

    // создание нового элемента
    $scope.create = function (item) {
        // HTTP POST
        // Отправка POST запроса для создания новой записи на сервере
        $http.post(baseUrl, item).success(function (item) {
            $scope.items.push(item);
            $scope.currentView = "table";
        });
    };

    // обновление элемента
    $scope.update = function (item) {
        // HTTP PUT
        // Отправка PUT запроса для обновления определенной записи на сервере
        $http({
            url: baseUrl + item.id,
            method: "PUT",
            data: item
        }).success(function (modifiedItem) {
            for (var i = 0; i < $scope.items.length; i++) {
                if ($scope.items[i].id === modifiedItem.id) {
                    $scope.items[i] = modifiedItem;
                    break;
                }
            }
            $scope.currentView = "table";
        });
    };

    // удаление элемента из модели
    $scope.delete = function (item) {
        // HTTP DELETE
        // отправка DELETE запроса по адресу http://localhost:2403/items/id что приводит к удалению записей на сервере
        $http({
            method: "DELETE",
            url: baseUrl + item.id
        }).success(function () {
            $scope.items.splice($scope.items.indexOf(item), 1);
        });
    };

    // редеактирование существующего или создание нового элемента
    $scope.editOrCreate = function (item) {
        $scope.currentItem = item ? angular.copy(item) : {};
        $scope.currentView = "edit";
    };

    // сохранение изменений
    $scope.saveEdit = function (item) {
        // Если у элемента есть свойство id выполняем редактирование
        // В данной реализации новые элементы не получают свойство id поэтому редактировать их невозможно (будет исправленно в слудующих примерах)
        if (angular.isDefined(item.id)) {
            $scope.update(item);
        } else {
            $scope.create(item);
        }
    };

    // отмена изменений и возврат в представление table
    $scope.cancelEdit = function () {
        $scope.currentItem = {};
        $scope.currentView = "table";
    };

    $scope.refresh();
});
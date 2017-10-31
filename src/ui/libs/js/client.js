
angular.module("dellCoinClient", [])
.constant("baseUrl", "http://localhost:8080/")
.controller("defaultCtrl", function ($scope, $http, $log, baseUrl) {

    // текущее педставление
    $scope.files = [];
    $scope.fileContent = "";

    $scope.addFile = function () {

        var file = {"content": $scope.fileContent, "id": null, "hash": ""};

        // $http.post(baseUrl + "file", file).success(function (item) {
        //     $scope.files.push(item);
        // });

        $log.info("Add file: " + file);

        $http({
            method: 'POST',
            url: baseUrl + "file",
            data: file
        }).then(function successCallback(response) {
            $scope.files.push(response.data);
            $scope.fileContent = "";
            $log.info("File added: " + response.data);
        }, function errorCallback(response) {
            $log.error(response);
        });
    };

    $scope.deleteFile = function (file) {

        $log.info("Delete file: " + file);

        $http({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            dataType: 'json',
            method: 'DELETE',
            url: baseUrl + "files/" + file.id,
            data: file
        }).then(function successCallback(response) {
            $scope.files.splice($scope.files.indexOf(file), 1);
            $log.info("File deleted.");
        }, function errorCallback(response) {
            $log.error(response);
        });
    };

    $scope.uploadFile = function (file) {

        $log.info("Upload file: " + file);
        $log.info("Create slices for file audit...");
        $http({
            method: 'POST',
            url: baseUrl + "slice",
            data: file
        }).then(function successCallback(response) {
            $scope.files.push(response.data);
            $scope.fileContent = "";
            $log.info("File slices created " + response.data);
        }, function errorCallback(response) {
            $log.error(response);
        });
    }

    $scope.init = function () {
        $http({
            method: 'GET',
            url: baseUrl + "files"
        }).then(function successCallback(response) {
            $scope.files = response.data;
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
    }

    // создание нового элемента
    $scope.create = function (item) {
        // HTTP POST
        // Отправка POST запроса для создания новой записи на сервере
        $http.post(baseUrl, item).success(function (item) {
            $scope.items.push(item);
            $scope.currentView = "table";
        });
    }

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
                if ($scope.items[i].id == modifiedItem.id) {
                    $scope.items[i] = modifiedItem;
                    break;
                }
            }
            $scope.currentView = "table";
        });
    }

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
    }

    // редеактирование существующего или создание нового элемента
    $scope.editOrCreate = function (item) {
        $scope.currentItem = item ? angular.copy(item) : {};
        $scope.currentView = "edit";
    }

    // сохранение изменений
    $scope.saveEdit = function (item) {
        // Если у элемента есть свойство id выполняем редактирование
        // В данной реализации новые элементы не получают свойство id поэтому редактировать их невозможно (будет исправленно в слудующих примерах)
        if (angular.isDefined(item.id)) {
            $scope.update(item);
        } else {
            $scope.create(item);
        }
    }

    // отмена изменений и возврат в представление table
    $scope.cancelEdit = function () {
        $scope.currentItem = {};
        $scope.currentView = "table";
    }

    $scope.refresh();
});
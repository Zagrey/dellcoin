pragma solidity ^0.4.0;


contract DellCoin {

    /* Array with all balances */

    mapping (address => uint256) public balanceOf;
    address private owner;
    uint8 public decimals = 18;

    function DellCoin() public {
        owner = msg.sender;

    }

    modifier onlyOwner {

        require(msg.sender == owner);
        _;
    }

    function transfer(address _to, uint256 _value) onlyOwner public {
        require(balanceOf[_to] + _value >= balanceOf[_to]);
        // Check for overflows

        balanceOf[_to] += _value;
        // Add the same to the recipient
    }

    function() public payable {
        buy();
    }

    function buy() public payable {

        balanceOf[msg.sender] += msg.value;
        // adds the amount to buyer's balance
    }

    function sell(uint amount) public {

        uint256 value = amount * 10 ** uint256(decimals);

        require(balanceOf[msg.sender] >= amount);
        // checks if the sender has enough to sell

        balanceOf[msg.sender] -= value;
        // subtracts the amount from seller's balance

        require(msg.sender.send(value));
        // sends ether to the seller: it's important to do this last to prevent recursion attacks

    }


    function kill() public onlyOwner {
        selfdestruct(owner);
    }
}


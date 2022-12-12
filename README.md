# BCPedia
An Online Content Publishment and Verification System

## Description
This system is a platform that provide following functions: content publishment, content update, content dispute, content verification. It offers users reliable content that is verified by validators, and the validators are elected by user community itself. The system’s functionality is based on the reliability and transparency characteristics of blockchain.

## Get Started
### Prerequisites
* Node.js (version = v16.18.0)
* Docker (version = 20.10.17) (Optional)
* Truffle (version = v5.5.21)
* Ganache (version = v7.4.4)
* Msyql (version = 8.0) (Optional, install Docker or Mysql)

### Deployment
#### On-chain
1. Clone the repository: `$ git clone https://github.com/snail-inO/BCPedia.git`
2. Go to the on-chain code directory: `$ cd BCPedia/onchain`
3. Install project dependencies: `$ npm install`
4. Compile solidity contracts: `$ truffle compile`
5. Start ganache: `$ ganache`
6. Deploy contracts to local blockchain: `$ truffle migrate --reset`
#### Off-chain
1. Clone the repository: `$ git clone https://github.com/snail-inO/BCPedia.git`
2. Go to the off-chain code directory: `$ cd BCPedia/offchain/BCPedia`
3. Start MySQL database on port `3306` using root password `123456` and create a schema named `bcpedia`
   1. Use docker to create a MySQL container: `$ docker run -p 3306:3306 --name=bcpedia -e MYSQL_ROOT_PASSWORD=123456 -e MYSQL_DATABASE=bcpedia --rm -d mysql`
   2. Use MySQL directly
4. Start off-chain server: `$ ./mvnw clean spring-boot:run`

### API Usage
#### On-chain interactions
The scripts are at directory: `$ cd BCPedia/offchain/scripts`
The project provides following scripts:
#####BCPedia.js
Provides interfaces to interact with BCPedia SCs, usage: `$ node BCPeida.js [contract name] [function name] [parameters]`
#####Forward.js
Provides interface for time and block accelaration of local ganache blockchain, usage: `$ node Forward.js [time|block] [blockNumber]`
#####Hash.js
Provides sha256 hashing interface, usage: `$ node Hash.js [input]`

#### Off-chain interaction
##### Proposal interfaces
`GET localhost:8080/proposal`: retrieve all proposals
`POST localhost:8080/proposal`: create a new proposal
`GET/DELETE localhost:8080/proposal/{pid}`: retrieve/delete specific proposal with proposal id = pid

##### Entry interfaces
`GET localhost:8080/entries`: retrieve all entries
`POST localhost:8080/entries`: create a new entry
`GET/DELETE localhost:8080/entries/{eid}`: retrieve/delete specific entry with entry id = pid
`PATCH localhost:8080/entries`: update a specific entry

##### Like interfaces
`POST localhost:8080/like/init?addr={SC address}&account={admin account}&key={admin private key}`: init the like submission function
`POST localhost:8080/like`: create a like counter for a specific entry
`GET localhost:8080/like`: retrieve all like counters
`POST localhost:8080/like/{entry id}?uid={user account}`: submit a like to a specific entry with a user account

### Demos
[BCPedia CreateEntry Proposal and Vote WorkFlow Demo](https://youtu.be/iZVTP1ses4A)
[BCPedia batchUpdateLikes Workflow Demo](https://youtu.be/3xgmd-C23o0)
[BCPedia Request Promote Demo](https://youtu.be/8kCTshtTzzs)
[BCPedia Resign Validator Demo](https://youtu.be/clWmIfgMNig)

## Licence
This project is licensed under the [MIT License](LICENSE).

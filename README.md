# Fake Data Generator

        ______    _            _____        _             _____                           _
       |  ____|  | |          |  __ \      | |           / ____|                         | |
       | |__ __ _| | _____    | |  | | __ _| |_ __ _    | |  __  ___ _ __   ___ _ __ __ _| |_ ___  _ __
       |  __/ _` | |/ / _ \   | |  | |/ _` | __/ _` |   | | |_ |/ _ \ '_ \ / _ \ '__/ _` | __/ _ \| '__|
       | | | (_| |   <  __/   | |__| | (_| | || (_| |   | |__| |  __/ | | |  __/ | | (_| | || (_) | |
       |_|  \__,_|_|\_\___|   |_____/ \__,_|\__\__,_|    \_____|\___|_| |_|\___|_|  \__,_|\__\___/|_|


      
 Usage:

 java -jar fake-data-generator-&lt;version&gt;.jar &lt;parallelism&gt; &lt;nb_of_users&gt; &lt;min_transactions&gt; &lt;max_transactions&gt;
 
 * **parallelism**: number of concurrent actors to generate data
 * **nb_of_users**: number of distinct users for the data set
 * **min_transactions**: minimum number of transactions for each user
 * **max_transactions**: maximum number of transactions for each user
 
 The program will generate **nb_of_users** users and for each user, between **min_transactions** and **max_transactions** transactions. The number of transactions is randomized between the given bounds
 


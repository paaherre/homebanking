# Java-Data HomeBanking

## MindHub HomeBanking Java Project: 
### https://jd-homebanking.herokuapp.com/index.html

***

This is a FullStack Project with back end on Java 11 SpringBoot Framework 2.4.4 and Vue Template as frontEnd.

Register as new user also creates a new account:

![Register](https://github.com/paaherre/homebanking/blob/main/src/main/resources/static/img/readme/register.png?raw=true)

![Accounts](https://github.com/paaherre/homebanking/blob/main/src/main/resources/static/img/readme/accounts.png?raw=true)

Welcome to Java-Data Bank!!
Thanks for join the team, now you can see the account data, delete accounts or create new accounts (must choose between checking accounts or savings accounts)

First, get some cash aplying to our pre-aproved loans, selecting the option in the account card or the option in the loans details on the bottom, then just complete the form.

There is a few operations you can make with your new account, like create a new Credit or Debit Card in the Cards Section:

![Cards](https://github.com/paaherre/homebanking/blob/main/src/main/resources/static/img/readme/cards.png?raw=true)

Cards Data is generated in backEnd where you must choose between Credit or Debit Card and the card Color. A random sequence of 16 numbers, your account information, expiration date (5 years since created) and a random CVV 3 digit number is asigned to identify.

Also we got a transfer section to make your transferences safety and comfortable:
![Transfer](https://github.com/paaherre/homebanking/blob/main/src/main/resources/static/img/readme/transfer.png?raw=true)

In this section you can fill the form to make a new transaction it can be between your own accounts or a third party account (you can create a new account or client).

For the most important banks like us, you must have a section to review all your transactions:

![Transaction](https://github.com/paaherre/homebanking/blob/main/src/main/resources/static/img/readme/transaction.png?raw=true)

Here you can see all the transactios that you have made in with your accounts, the filter works in backend just sending the dates and account number, by default when you select the account it fills the past 3 days and retrieve the information, also you can download a PDF extraction generated guess where... yes, on java!. (thanks openPDF and IText library):

![PDF](https://github.com/paaherre/homebanking/blob/main/src/main/resources/static/img/readme/pdf.png?raw=true)

***

In order to improve your visit, here you go 2 accounts to make some tests:

Client
user: melba@mindhub.com
password: Password1

Admin
user: admin@admin.com
password: admin

The admin only works to create a new type of loans, selecting the name, max amount allowed and payments.

I hope you enjoy my project.

Thanks for your time.

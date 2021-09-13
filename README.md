# Java-Data HomeBanking

## MindHub HomeBanking Java Project: 
### https://jd-homebanking.herokuapp.com/index.html

***

This is a FullStack Project with back end on Java 11 SpringBoot Framework 2.4.4 and Vue Template as frontEnd.

Registering as new user also creates a new account:

![Register](https://github.com/paaherre/homebanking/blob/main/src/main/resources/static/img/readme/register.png?raw=true)

![Accounts](https://github.com/paaherre/homebanking/blob/main/src/main/resources/static/img/readme/accounts.png?raw=true)

Welcome to Java-Data Bank!!
Thanks for joining the team, now you can see the account information, delete or create new accounts (you must choose between checking accounts or savings accounts)

First, get some cash by aplying to our pre-aproved loans, selecting the option in the account card or the option in the loans details on the bottom, then just complete the form.

There are a few operations you can make with your new account, like creating a new card (either Credit or Debit) in the card section:

![Cards](https://github.com/paaherre/homebanking/blob/main/src/main/resources/static/img/readme/cards.png?raw=true)

After choosing the card type and color, Data and information are generated in the backEnd. A random 16 digits sequence as an unique identifier, expiration date (5 years since creation) and random CVV, are asigned to the new card.

Additionally, we have a transfer section, where you can operate with safety and comfort::
![Transfer](https://github.com/paaherre/homebanking/blob/main/src/main/resources/static/img/readme/transfer.png?raw=true)

In this section, you've to fill the form, by choosing a transfer between your own accounts, or a thrid-party account.

Last but not least, there's a section to review all your previous transactions!:

![Transaction](https://github.com/paaherre/homebanking/blob/main/src/main/resources/static/img/readme/transaction.png?raw=true)

Here you can see all the transactios that you have made. Just pick the dates and the account number, and by default, the backEnd will bring up the last 3 days of information but you can select the dates manually.
In addition,  you can download a PDF with the requested information!  Generated guess where... yes, on java!. (thanks openPDF and IText libraries):

![PDF](https://github.com/paaherre/homebanking/blob/main/src/main/resources/static/img/readme/pdf.png?raw=true)

***

In order to improve your visit, here you have 2 accounts to make some tests:

Client:
user: melba@mindhub.com
password: Password1

Admin:
user: admin@admin.com
password: admin

The admin only works to create a new type of loans, selecting the name, max amount allowed and payments.

I hope you enjoy my project.

Thanks for your time!.

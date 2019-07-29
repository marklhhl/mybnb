delete from user;
delete from listing;
delete from renter_comment;
delete from list_comment;
delete from calendar;
delete from history;

insert into user
values
	(1,'531 george st', 'Canada', 'Toronto', 'M2M3A6', str_to_date('august 10 1994', '%M %d %Y'), 232133333, 'james', 'charles','eva', 'writer', 1555165125523333, 155516512552333, 'someemail@hotmail.com', '123'),
	(2,'23 mintwood drive', 'Canada', 'Toronto', 'M2M3A6', str_to_date('december 16 1998', '%M %d %Y'), 321542111, 'michael', 'chen',null, 'computer science', 1555165125523333, 155516512552333, 'someemail2@hotmail.com', '123'),
	(3,'52 church st', 'Usa', 'New York', 'M2M3A6', str_to_date('august 10 1994', '%M %d %Y'), 653246123, 'john', 'mcnugget',null, 'donut eater', 1555165125523333, 155516512552333, 'someemail3@hotmail.com', '123'),
	(4,'1324 st george st', 'Canada', 'Toronto', 'M2M3A6', str_to_date('september 10 1990', '%M %d %Y'), 644257123, 'some', 'guy',null, 'cscb07 torturer', 1555165125523333, 155516512552333, 'someemail4@hotmail.com', '123'),
	(5,'923 notaname ko', 'Japan', 'Toyko', 'M2M3A6', str_to_date('august 10 1970', '%M %d %Y'), 653213612, 'Joe', 'Bettrige',null, 'salary man', 1555165125523333, 155516512552333, 'someemail5@hotmail.com', '123');

insert into listing
value
	(1, 'house', 123.000000, 23.000000, 'Toronto', '123skymark dr', 'M2M3A6', 'Canada', 'YES', 4, 1, 1, 1, null, null, 1),
	(2, 'house', 121.000000, 23.000000, 'Toronto', '23nordivir wy', 'M2M3A6', 'Canada', 'YES', 9, 1, 1, 1, null, null, 3),
	(3, 'house', 1.000000, 2.000000, 'middleof', 'nowhere', 'M2M3A6', 'Canada', 'YES', 2, 1, 1, 1, 'free breakfast', 'why did you come here', 3),
	(4, 'house', 1.000000, 2.000000, 'middleof', 'nowhere', 'M2M3A6', 'USA', 'YES', 2, 1, 1, 1, 'free breakfast', 'why did you come here', 3);


insert into renter_comment
values
	(11, 1, str_to_date('may 16 2019', '%M %d %Y'), 'this guy suck', 1, 5);
    
insert into list_comment
values
	(22, 5, str_to_date('may 13 2019', '%M %d %Y'), 'very yellow house', 5, 1),
	(33, 5, str_to_date('may 13 2019', '%M %d %Y'), 'very yellow house', 5, 1),
    	(66, 5, str_to_date('may 13 2019', '%M %d %Y'), 'very yellow house', 5, 1),
    	(44, 5, str_to_date('may 13 2019', '%M %d %Y'), 'very clean house', 5, 1),
            	(55, 5, str_to_date('may 13 2019', '%M %d %Y'), 'very clean house', 5, 1),
                	(77, 5, str_to_date('may 13 2019', '%M %d %Y'), 'cool house', 5, 1);
                    
insert into calendar
values
	(111, str_to_date('may 16 2019', '%M %d %Y'), str_to_date('may 22 2019', '%M %d %Y'), 300, 1),
	(222, str_to_date('october 23 2019', '%M %d %Y'), str_to_date('october 30 2019', '%M %d %Y'), 500, 2),
	(333, str_to_date('october 17 2019', '%M %d %Y'), str_to_date('december 30 2019', '%M %d %Y'), 500, 3);


insert into history
values
	(1111, str_to_date('september 16 2019', '%M %d %Y'), str_to_date('september 22 2019', '%M %d %Y'), str_to_date('June 1 2019', '%M %d %Y'), 250, 1750, 'Canceled', 1, 5, 2),
	(2222, str_to_date('June 4 2019', '%M %d %Y'), str_to_date('June 12 2019', '%M %d %Y'), str_to_date('april 1 2019', '%M %d %Y'), 250, 1750, 'Canceled', 3, 4, 3),
	(3333, str_to_date('august 9 2019', '%M %d %Y'), str_to_date('august 12 2019', '%M %d %Y'), str_to_date('april 1 2019', '%M %d %Y'), 250, 750, 'Canceled', 3, 5, 3),
	(4444, str_to_date('september 23 2019', '%M %d %Y'), str_to_date('september 24 2019', '%M %d %Y'), str_to_date('June 2 2019', '%M %d %Y'), 250, 1750, 'Canceled', 1, 5, 2),
	(5555, str_to_date('august 9 2019', '%M %d %Y'), str_to_date('august 12 2019', '%M %d %Y'), str_to_date('april 1 2019', '%M %d %Y'), 250, 750, 'Canceled', 3, 4, 3),
    	(6666, str_to_date('august 9 2019', '%M %d %Y'), str_to_date('august 12 2019', '%M %d %Y'), str_to_date('april 1 2019', '%M %d %Y'), 250, 750, 'Canceled', 3, 2, 3),
	(7777, str_to_date('august 9 2019', '%M %d %Y'), str_to_date('august 12 2019', '%M %d %Y'), str_to_date('april 1 2019', '%M %d %Y'), 250, 750, 'Canceled', 3, 2, 3),

	(8888, str_to_date('august 9 2019', '%M %d %Y'), str_to_date('august 12 2019', '%M %d %Y'), str_to_date('april 1 2019', '%M %d %Y'), 250, 750, 'Canceled', 3, 2, 3),
    (9999, str_to_date('august 9 2019', '%M %d %Y'), str_to_date('august 12 2019', '%M %d %Y'), str_to_date('april 1 2019', '%M %d %Y'), 250, 750, 'Canceled', 3, 2, 3);

select * from listing;
select * from user;
select * from renter_comment;
select * from list_comment;
select * from calendar;
select * from history;
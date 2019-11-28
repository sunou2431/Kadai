package com.ripplex.unixtime;

public class UnixTime {
	//クラス変数
	int utime;//UNIX時間を保存する

	//UNIX時間「０」を保持するインスタンスを作成するコンストラクタ
	public UnixTime() {
		this.setUnixTime(0);
	}

	//UNIX時刻を符号あり32ビット整数の引数として受け取るコンストラクタ
	public UnixTime(int utime) {
		this.setUnixTime(utime);
	}

	//時刻をUNIX時刻(符号あり32ビット整数)でセットするメソッド
	public void setUnixTime(int utime) {
		//int型の最大値を超えた場合エラーの出力
		try {
			Math.incrementExact(utime - 1);
			this.utime = utime;
		}
		catch(Exception e) {
			System.out.println("Int型の最大値を超えました");
		}
	}

	//保持しているUNIX時間を得るメソッド
	public int getUnixTime() {
		return this.utime;
	}

	//保持しているUNIX時刻から西暦の「年」を得るメソッド
	public int getYear() {
		return this.Keisan(0);
	}

	//保持しているUNIX時刻から、西暦の「月」を得るメソッド
	//daysから月を割り出す
	public int getMonth() {
		return this.Keisan(1);
	}

	//保持しているUNIX時刻から、西暦の「日」を得るメソッド
	public int getDay() {
		return this.Keisan(2);
	}

	//送られてきたutimeを比べてまだ続行出来るか判定
	public boolean hantei(int utime){
		if((utime > this.utime) && (this.utime >= 0)){
			return true;
		}
		if((utime * -1 <= this.utime) && (this.utime < 0)) {
			return true;
		}
		return false;
	}

	//日付、年をUNIX時刻から計算して返す
	//引数0の場合は年を返す、１の場合は月を返す、２の場合日にち
	public int Keisan(int ver) {
		//変数宣言
		int utime = 0;
		int year;
		int days = 0;

		//UNIX時刻がマイナスの場合1969年から減っていく。+の時は1970から増える
		if(this.utime > 0) {
			year = 1970;
		}
		else {
			year = 1969;
		}

		while(true) {
			utime = utime + 86400;

			//まだ続けられるか判定
			if(this.hantei(utime)){
				//それぞれ何を返すか判断し返す
				switch(ver) {
				case 0:
					return year;
				case 1:
					return this.Keisan_month(year, days + 1 ,0);
				case 2:
					return this.Keisan_month(year, days + 1 ,1);
				}
			}

			//日にち足して
			//うるう年じゃないか確認してyearを増減
			//厳密に欠けば100の倍数はうるう年ではないが簡略
			days++;
			if(days >= 365) {
				if(year % 4  != 0){
					days = 0;
					if(this.utime >= 0) {
						year++;
					}
					else {
						year--;
					}
				}
				else if(days >= 366) {
					days = 0;
					if(this.utime >= 0) {
						year++;
					}
					else {
						year--;
					}
				}
			}
		}
	}

	//月と日にちの計算をする
	//0で月、1で日を返す
	public int Keisan_month(int year,int days ,int ver) {
		//変数宣言
		int data[] = {31,28,31,30,31,30,31,31,30,31,30,31};//各月の最大日数
		int month;

		//うるう年の場合2月を29日する
		if(year % 4 == 0) {
			data[1] = 29;
		}

		//UNIX時刻がマイナスの場合12月スタート
		if(this.utime >= 0) {
			month = 0;
		}
		else {
			month = 11;
		}

		while(true) {
			//ひと月の最大日数と残り日数を比べる
			//引けない場合 値を返す
			if(days > data[month]) {
				days = days - data[month];

				if(this.utime >= 0) {
					month++;
				}
				else {
					month--;
				}
			}
			else {
				if(ver == 0) {
					return month + 1;
				}
				else {
					if(this.utime >= 0) {
						return days;
					}
					else {
						return data[month] - days + 1;
					}
				}
			}
		}
	}
}


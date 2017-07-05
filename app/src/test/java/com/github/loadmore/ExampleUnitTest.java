package com.github.loadmore;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {



    @Test
    public void aaaa() throws Exception {
        int a=2;
        if (a==1){
            System.out.println("1======"+a);
        }else if(a==2){
            System.out.println("2======"+a);
        }else{
            System.out.println("3======"+a);
        }
        a++;
    }





    @Test
    public void a() throws Exception {
        String a = AES.decode("7DD151868A70E160E4494D6DADCBC1055ECACFBC8E6C023D1A2F0AA06EADBFE5DA53F008ABED90E934D7864E06C7F00E");
        System.out.println(a);
    }
    @Test
    public void addition_isCorrect() throws Exception {
        String a = AES.encode("海淘科技延法超拖欠员工工资");
        System.out.println(a);
    }
}
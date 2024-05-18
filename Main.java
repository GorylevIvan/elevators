import java.util.*;

class Shared {
    int floor;

    public synchronized void share(int floor) {
        this.floor = floor;
    }

    public synchronized void share_position1(int floor) {
        this.floor = floor;
    }

    public synchronized int get() {
        return floor;
    }

    public synchronized int get_position1() {
        return floor;
    }

    public synchronized void share_position2(int floor) {
        this.floor = floor;
    }

    public synchronized int get_position2() {
        return floor;
    }
}

public class Main {
    static Shared hz = new Shared();
    static boolean stop = false;

    public static void main(String[] args) {
        Generate generate = new Generate();
        First first = new First();
        Second second = new Second();

        generate.start();
        first.start();
        second.start();
    }

    static class Generate extends Thread {
        public void run() {
            int floor_num = 15;
            int calls;
            Random random = new Random();
            Random rand_time = new Random();

            while (!stop) {
                int sl = rand_time.nextInt(5, 15);
                try {
                    sleep(sl * 1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                int num = random.nextInt(1, floor_num);
                calls = num;
                System.out.println(num);
                hz.share(calls);
            }
        }
    }

    static class First extends Thread {
        public void run() {
            int cur_pos = 1, way = 0; // 0 - стоит, 1 - вверх, 2 - вниз

            while (!stop) {
                hz.share_position1(cur_pos);
                int need_floor = hz.get();
                int sec_pos = hz.get_position2();
                if (Math.abs(cur_pos - need_floor) <= Math.abs(sec_pos - need_floor) && need_floor > 0) {
                    if (cur_pos < need_floor) {
                        way = 2;
                        while (cur_pos != need_floor) {
                            cur_pos -= 1;
                            try {
                                sleep(1000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            System.out.print("1: floor is ");
                            System.out.println(cur_pos);
                        }
                    } else if (cur_pos > need_floor) {
                        way = 1;
                        while (cur_pos != need_floor) {
                            cur_pos += 1;
                            try {
                                sleep(1000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            System.out.print("1: floor is ");
                            System.out.println(cur_pos);
                        }
                    }
                    way = 2;
                    while (cur_pos != 1) {
                        cur_pos -= 1;
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        System.out.print("1: floor is ");
                        System.out.println(cur_pos);
                    }
                    way = 0;
                }
            }
        }
    }

    static class Second extends Thread {
        public void run() {
            int cur_pos = 1, way = 0;

            while (!stop) {
                int need_floor = hz.get();
                int fir_pos = hz.get_position1();
                if (Math.abs(cur_pos - need_floor) > Math.abs(fir_pos - need_floor) && need_floor > 0) {
                    if (cur_pos < need_floor) {
                        way = 1;
                        while (cur_pos != need_floor) {
                            cur_pos += 1;
                            try {
                                sleep(1000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            System.out.print("2: floor is ");
                            System.out.println(cur_pos);
                        }
                    } else if (cur_pos > need_floor) {
                        way = 2;
                        while (cur_pos != need_floor) {
                            cur_pos -= 1;
                            try {
                                sleep(1000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            System.out.print("2: floor is ");
                            System.out.println(cur_pos);
                        }
                    }
                    way = 2;
                    while (cur_pos != 1) {
                        cur_pos -= 1;
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        System.out.print("2: floor is ");
                        System.out.println(cur_pos);
                    }
                    way = 0;
                }
            }
        }
    }
}

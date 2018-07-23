package com.cmos.audiotransfer.transfermanager.service;

import com.google.common.base.Function;
import com.google.common.util.concurrent.AbstractExecutionThreadService;

/**
 * Created by hejie
 * Date: 18-7-23
 * Time: 下午3:53
 * Description:
 */
public class Service extends AbstractExecutionThreadService {
    public String name = "service";

    public Service() {

    }

    public Service(String name) {
        this.name = name;
    }

    public Function running = new Function() {

        @Override
        public Object apply(Object object) {
            System.out.println(name + " is running");
            return object;
        }
    };

    @Override
    protected void startUp() {
        System.out.println(name + " startup");
    }

    @Override
    protected void run() throws Exception {
        while (isRunning()) {

            running.apply(this);

            Thread.sleep(1000);
        }
    }

    @Override
    protected void shutDown() throws Exception {
        System.out.println(name + " stop");
    }

    public static void main(String[] args) {

        Service service = new Service("data process") {

            @Override
            protected void startUp() {
                super.startUp();
            }

            @Override
            protected void shutDown() throws Exception {
                super.shutDown();
            }

        };

        service.running = new Function() {

            @Override
            public Object apply(Object service) {

                System.out.println("aaaaaa");
                return service;
            }
        };

        service.startAsync();

        System.out.println("start async...");

    }

}

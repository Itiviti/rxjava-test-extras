package com.ullink.rxjava.testextras.scheduler

import rx.functions.Action0
import spock.lang.Specification

import java.util.concurrent.TimeUnit


class RxDemoSchedulerSpecs extends Specification {

    def "I can still schedule tasks"() {
        setup:
        RxDemoScheduler scheduler = new RxDemoScheduler()

        when:
        scheduler.createWorker().schedule(new Action0() {
            @Override
            void call() {
                //well I'm doing nothing
            }
        }, 1, TimeUnit.SECONDS)

        then:
        noExceptionThrown()
    }

    def "I can still manipulate time by advancing it"() {
        setup:
        RxDemoScheduler scheduler = new RxDemoScheduler()
        int counter = 0
        scheduler.createWorker().schedule(new Action0() {
            @Override
            void call() {
                counter++
            }
        }, 1, TimeUnit.SECONDS)

        when:
        scheduler.advanceTimeBy(1,TimeUnit.MILLISECONDS)

        then:
        scheduler.now() == 1
        counter == 0

        when:
        scheduler.advanceTimeBy(1,TimeUnit.SECONDS)

        then:
        scheduler.now() == 1001
        counter == 1
    }

    def "I can still manipulate time by advancing it to a specific moment"() {
        setup:
        RxDemoScheduler scheduler = new RxDemoScheduler()
        int counter = 0
        scheduler.createWorker().schedule(new Action0() {
            @Override
            void call() {
                counter++
            }
        }, 1, TimeUnit.SECONDS)

        when:
        scheduler.advanceTimeTo(1,TimeUnit.MILLISECONDS)

        then:
        scheduler.now() == 1
        counter == 0

        when:
        scheduler.advanceTimeTo(3,TimeUnit.MILLISECONDS)

        then:
        scheduler.now() == 3
        counter == 0

        when:
        scheduler.advanceTimeTo(1,TimeUnit.SECONDS)

        then:
        scheduler.now() == 1000
        counter == 1

    }

}

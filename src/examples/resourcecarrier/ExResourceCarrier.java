package examples.resourcecarrier;

public class ExResourceCarrier {

    public static void main(String[] args) {

        ResourceCarrier carrier = new ResourceCarrier();
        ThingNeedingResource thingNeedingResource = new ThingNeedingResource(carrier);
        ThingNeedingResource thingNeedingResource2 = new ThingNeedingResource(carrier);
        ThingNeedingResource thingNeedingResource3 = new ThingNeedingResource(carrier);
        ResourceCreator resourceCreator = new ResourceCreator(carrier);

        new Thread(thingNeedingResource).start();
        new Thread(thingNeedingResource2).start();
        new Thread(thingNeedingResource3).start();
        new Thread(resourceCreator).start();
    }
}

    public class ResourceCarrier {

        private boolean resourceReady;

        public boolean isResourceReady() {
            return resourceReady;
        }

        public void resourceIsReady() {
            resourceReady = true;
        }
    }

    public class ResourceCreator implements Runnable {

        private ResourceCarrier carrier;

        public ResourceCreator(ResourceCarrier carrier) {
            this.carrier = carrier;
        }

        @Override

        public void run() {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (carrier) {
                carrier.resourceIsReady();
                carrier.notifyAll();
            }
        }
    }

    public class ThingNeedingResource implements Runnable {

        private ResourceCarrier carrier;

        public ThingNeedingResource(ResourceCarrier carrier) {

            this.carrier = carrier;
        }

        @Override
        public void run() {
            synchronized (carrier) {
                while (!carrier.isResourceReady()) {
                    try {
                        System.out.println("Waiting for Resource");
                        carrier.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("haz resource");
            }
        }
    
}

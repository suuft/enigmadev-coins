package net.swiftysweet.coins.mysql;

public interface ResponseHandler<R, O, T extends Throwable> {

    R handleResponse(O o) throws T;
}

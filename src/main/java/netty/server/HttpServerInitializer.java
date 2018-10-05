package netty.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.cors.CorsConfig;
import io.netty.handler.codec.http.cors.CorsConfigBuilder;
import io.netty.handler.ssl.SslContext;

public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {
    private final CorsConfig corsConfig;

    private final SslContext sslContext;

    public HttpServerInitializer(SslContext sslContext) {
        this.sslContext = sslContext;
        corsConfig = CorsConfigBuilder
                .forAnyOrigin()
                .allowNullOrigin()
                .allowCredentials()
                .build();
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline p = ch.pipeline();
        if (sslContext != null) {
            p.addLast(sslContext.newHandler(ch.alloc()));
        }
        p.addLast(new HttpRequestDecoder())
                //.addLast(new HttpObjectAggregator(1048576))
                .addLast(new HttpResponseEncoder())
                .addLast(new HttpServerHandler());
        /*ch.pipeline()
                .addLast(new HttpResponseEncoder())
                .addLast(new HttpRequestDecoder())
                .addLast(new HttpObjectAggregator(Integer.MAX_VALUE))
                .addLast(new CorsHandler(corsConfig))
                //.addLast(new FilterHandler())
                .addLast(new WorkerHandler());*/
    }
}

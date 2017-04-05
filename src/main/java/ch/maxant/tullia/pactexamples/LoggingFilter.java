package ch.maxant.tullia.pactexamples;

import javax.annotation.Priority;
import javax.ws.rs.ConstrainedTo;
import javax.ws.rs.RuntimeType;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

//http://stackoverflow.com/questions/38459096/how-to-get-plain-xml-from-javax-ws-rs-entity-on-client-side/38459381#38459381
@Priority(Integer.MIN_VALUE)
@ConstrainedTo(RuntimeType.CLIENT)
public class LoggingFilter implements ClientRequestFilter, WriterInterceptor {

    private static final Logger LOGGER = 
                                    Logger.getLogger(LoggingFilter.class.getName());

    private static final String ENTITY_STREAM_PROPERTY = 
                                    LoggingFilter.class.getName() + ".entityLogger";

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private static final int MAX_ENTITY_SIZE = 1024 * 8;

    private void log(StringBuilder sb) {
        LOGGER.info(sb.toString());
    }

    @Override
    public void filter(ClientRequestContext context) throws IOException {

        if (context.hasEntity()) {
            OutputStream stream = new LoggingStream(context.getEntityStream());
            context.setEntityStream(stream);
            context.setProperty(ENTITY_STREAM_PROPERTY, stream);
        }
    }

    @Override
    public void aroundWriteTo(WriterInterceptorContext context) 
                throws IOException, WebApplicationException {

        LoggingStream stream = (LoggingStream) 
                                   context.getProperty(ENTITY_STREAM_PROPERTY);
        context.proceed();
        if (stream != null) {
            log(stream.getStringBuilder(DEFAULT_CHARSET));
        }
    }

    private class LoggingStream extends FilterOutputStream {

        private final StringBuilder b = new StringBuilder();
        private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        LoggingStream(final OutputStream inner) {
            super(inner);
        }

        StringBuilder getStringBuilder(final Charset charset) {
            // write entity to the builder
            final byte[] entity = baos.toByteArray();

            b.append(new String(entity, 0, 
                                Math.min(entity.length, MAX_ENTITY_SIZE), charset));
            if (entity.length > MAX_ENTITY_SIZE) {
                b.append("...more...");
            }
            b.append('\n');

            return b;
        }

        @Override
        public void write(final int i) throws IOException {
            if (baos.size() <= MAX_ENTITY_SIZE) {
                baos.write(i);
            }
            out.write(i);
        }
    }
}
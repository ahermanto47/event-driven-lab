// kubectl create secret generic postgresql-datasource --from-file=datasource.properties
//
// kamel run RestDSL.java -t knative-service.max-scale=3 -t tracing.enabled=true --build-property quarkus.datasource.camel.db-kind=postgresql --config secret:postgresql-datasource -d mvn:io.quarkus:quarkus-jdbc-postgresql:2.7.0.Final

import org.apache.camel.Exchange;

public class RestDSL extends org.apache.camel.builder.RouteBuilder {
	@Override
	public void configure() throws Exception {
		rest().get("/").to("direct:hello");

		from("direct:hello").setHeader(Exchange.CONTENT_TYPE, constant("text/plain")).transform().simple("hello");

		rest().post("/contact").to("direct:insert");

		from("direct:insert").log("${body}").setHeader("id").jsonpath("$.Id", true).setHeader("accountId")
				.jsonpath("$.AccountId", true).setHeader("firstName").jsonpath("$.FirstName", true)
				.setHeader("lastName").jsonpath("$.LastName", true).setHeader("phone").jsonpath("$.Phone", true)
				.setHeader("email").jsonpath("$.Email", true)
				.setBody(simple(
						"insert into sfdc_contact values ('${header.id}', '${header.accountId}', '${header.firstName}', '${header.lastName}', '${header.phone}', '${header.email}')"))
				.to("jdbc:camel").log("insert successful - ${body}")
				.setHeader(Exchange.CONTENT_TYPE, constant("text/plain")).transform().simple("success");
	}
}
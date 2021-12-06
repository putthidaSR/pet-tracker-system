package tcss559.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyComponentPathImpl;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtils {
	private static final SessionFactory sessionFactory;
	static {
		try {
			StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder().configure().build();
			Metadata metadata = new MetadataSources(standardRegistry).getMetadataBuilder()
					.applyImplicitNamingStrategy(ImplicitNamingStrategyComponentPathImpl.INSTANCE).build();
			sessionFactory = metadata.getSessionFactoryBuilder().build();
		}catch (Throwable ex) {
			ex.printStackTrace();
			throw new ExceptionInInitializerError(ex);
		}
	}
	
	public static Session getSession() throws HibernateException {
		return sessionFactory.openSession();
	}
}

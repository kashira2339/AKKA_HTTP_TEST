package dao

import connection.MySQLDBImpl
import model.{Dog, DogTable}

import scala.concurrent.Future

trait DogDao extends DogTable with MySQLDBImpl {

  import driver.api._

  protected val dogTableQuery = TableQuery[DogTable]

  def create(dog: Dog): Future[Int] = db.run {
    dogTableAutoInc += dog
  }


  def update(dog: Dog): Future[Int] = db.run {
    dogTableQuery.filter(_.id === dog.id.get).update(dog)
  }


  def getById(id: Int): Future[Option[Dog]] = db.run {
    dogTableQuery.filter(_.id === id).result.headOption
  }


  def getAll: Future[List[Dog]] = db.run {
    dogTableQuery.to[List].result
  }


  def delete(id: Int): Future[Int] = db.run {
    dogTableQuery.filter(_.id === id).delete
  }

  def ddl = db.run {
    dogTableQuery.schema.create
  }

  protected def dogTableAutoInc = dogTableQuery returning dogTableQuery.map(_.id)

}
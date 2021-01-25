var express = require('express');
var router = express.Router();
var Query = require('../controllers/query')


router.get('/', function(req, res) {
  Query.index()
  .then(dados => res.render('index',{activity:dados[0],datafiles:dados[1],tablespaces:dados[2],users:dados[3]}))
  .catch(e => res.status(500).jsonp({error: e}))
});
router.get('/activity/:id', function(req, res) {
  Query.consultarActivity(req.params.id)
  .then(dados => res.render('activity',{activity:dados}))
  .catch(e => res.status(500).jsonp({error: e}))
});
router.get('/datafile/:id', function(req, res) {
  Query.consultarDatafiles(req.params.id)
  .then(dados => res.render('datafile',{datafile:dados}))
  .catch(e => res.status(500).jsonp({error: e}))
});
router.get('/tablespace/:id', function(req, res) {
  Query.consultarTablespaces(req.params.id)
  .then(dados => res.render('tablespace',{tablespace:dados}))
  .catch(e => res.status(500).jsonp({error: e}))
});
router.get('/user/:id', function(req, res) {
  Query.consultarUsers(req.params.id)
  .then(dados => res.render('user',{user:dados}))
  .catch(e => res.status(500).jsonp({error: e}))
});

router.get('/activities', function(req, res) {
  Query.activity()
  .then(dados => res.render('activities',{activity:dados}))
  .catch(e => res.status(500).jsonp({error: e}))
});
router.get('/datafiles', function(req, res) {
  Query.datafiles()
  .then(dados => res.render('datafiles',{datafiles:dados}))
  .catch(e => res.status(500).jsonp({error: e}))
});
router.get('/tablespaces', function(req, res) {
  Query.tablespaces()
  .then(dados => res.render('tablespaces',{tablespaces:dados}))
  .catch(e => res.status(500).jsonp({error: e}))
});
router.get('/users', function(req, res) {
  Query.users()
  .then(dados => res.render('users',{users:dados}))
  .catch(e => res.status(500).jsonp({error: e}))
});

module.exports = router;
